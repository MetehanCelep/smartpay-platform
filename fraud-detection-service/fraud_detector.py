import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestClassifier, IsolationForest
from sklearn.preprocessing import StandardScaler
import joblib
import os
from datetime import datetime
import json

class FraudDetector:
    def __init__(self):
        self.model_path = 'models/fraud_model.pkl'
        self.scaler_path = 'models/scaler.pkl'
        self.isolation_forest = None
        self.rf_classifier = None
        self.scaler = None
        
        # Create models directory if not exists
        os.makedirs('models', exist_ok=True)
        
        # Load or initialize models
        self.load_models()
    
    def load_models(self):
        """Load pre-trained models or create new ones"""
        try:
            if os.path.exists(self.model_path) and os.path.exists(self.scaler_path):
                self.rf_classifier = joblib.load(self.model_path)
                self.scaler = joblib.load(self.scaler_path)
                print("Models loaded successfully")
            else:
                print("No pre-trained models found, initializing new models")
                self.initialize_models()
        except Exception as e:
            print(f"Error loading models: {e}")
            self.initialize_models()
    
    def initialize_models(self):
        """Initialize new ML models"""
        self.rf_classifier = RandomForestClassifier(
            n_estimators=100,
            max_depth=10,
            random_state=42
        )
        self.isolation_forest = IsolationForest(
            contamination=0.1,
            random_state=42
        )
        self.scaler = StandardScaler()
        
        # Train with dummy data for initialization
        dummy_data = self._generate_dummy_training_data()
        self._fit_models(dummy_data)
    
    def _generate_dummy_training_data(self):
        """Generate synthetic training data"""
        np.random.seed(42)
        n_samples = 1000
        
        # Normal transactions
        normal_amounts = np.random.lognormal(4, 1, int(n_samples * 0.95))
        normal_hours = np.random.randint(6, 23, int(n_samples * 0.95))
        normal_labels = np.zeros(int(n_samples * 0.95))
        
        # Fraudulent transactions
        fraud_amounts = np.random.lognormal(6, 1.5, int(n_samples * 0.05))
        fraud_hours = np.random.choice([0, 1, 2, 3, 4, 23], int(n_samples * 0.05))
        fraud_labels = np.ones(int(n_samples * 0.05))
        
        amounts = np.concatenate([normal_amounts, fraud_amounts])
        hours = np.concatenate([normal_hours, fraud_hours])
        labels = np.concatenate([normal_labels, fraud_labels])
        
        return pd.DataFrame({
            'amount': amounts,
            'hour': hours,
            'is_fraud': labels
        })
    
    def _fit_models(self, df):
        """Fit models with training data"""
        X = df[['amount', 'hour']]
        y = df['is_fraud']
        
        # Fit scaler
        X_scaled = self.scaler.fit_transform(X)
        
        # Fit classifier
        self.rf_classifier.fit(X_scaled, y)
        
        # Save models
        joblib.dump(self.rf_classifier, self.model_path)
        joblib.dump(self.scaler, self.scaler_path)
        
        print("Models trained and saved successfully")
    
    def extract_features(self, transaction_data):
        """Extract features from transaction data"""
        amount = float(transaction_data['amount'])
        timestamp = transaction_data.get('timestamp', datetime.now().isoformat())
        
        # Parse timestamp
        try:
            dt = datetime.fromisoformat(timestamp.replace('Z', '+00:00'))
        except:
            dt = datetime.now()
        
        hour = dt.hour
        day_of_week = dt.weekday()
        
        features = {
            'amount': amount,
            'hour': hour,
            'day_of_week': day_of_week,
            'is_weekend': 1 if day_of_week >= 5 else 0,
            'is_night': 1 if hour < 6 or hour > 22 else 0
        }
        
        return features
    
    def analyze(self, transaction_data):
        """Analyze transaction for fraud"""
        features = self.extract_features(transaction_data)
        
        # Basic feature set for model
        X = np.array([[features['amount'], features['hour']]])
        X_scaled = self.scaler.transform(X)
        
        # Get prediction and probability
        prediction = self.rf_classifier.predict(X_scaled)[0]
        probability = self.rf_classifier.predict_proba(X_scaled)[0]
        
        fraud_score = probability[1] if len(probability) > 1 else 0.0
        
        # Rule-based checks
        risk_factors = []
        
        if features['amount'] > 10000:
            risk_factors.append('High amount transaction')
            fraud_score += 0.1
        
        if features['is_night']:
            risk_factors.append('Transaction at unusual hours')
            fraud_score += 0.05
        
        if features['is_weekend']:
            risk_factors.append('Weekend transaction')
            fraud_score += 0.02
        
        # Cap fraud score at 1.0
        fraud_score = min(fraud_score, 1.0)
        
        # Determine risk level
        if fraud_score >= 0.8:
            risk_level = 'HIGH'
            action = 'BLOCK'
        elif fraud_score >= 0.5:
            risk_level = 'MEDIUM'
            action = 'REVIEW'
        else:
            risk_level = 'LOW'
            action = 'APPROVE'
        
        return {
            'transactionId': transaction_data.get('transactionId', 'unknown'),
            'fraudScore': round(fraud_score, 3),
            'riskLevel': risk_level,
            'recommendation': action,
            'riskFactors': risk_factors,
            'features': features
        }
    
    def train_model(self):
        """Retrain model with new data"""
        # In production, this would fetch real transaction data
        # For now, we retrain with dummy data
        dummy_data = self._generate_dummy_training_data()
        self._fit_models(dummy_data)
        return True