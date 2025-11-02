from flask import Flask, request, jsonify
from flask_cors import CORS
from fraud_detector import FraudDetector
import os
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)
CORS(app)

# Initialize fraud detector
detector = FraudDetector()

@app.route('/health', methods=['GET'])
def health():
    return jsonify({
        'status': 'UP',
        'service': 'fraud-detection-service'
    }), 200

@app.route('/api/fraud/analyze', methods=['POST'])
def analyze_transaction():
    try:
        data = request.get_json()
        
        # Validate required fields
        required_fields = ['amount', 'merchantId', 'timestamp']
        if not all(field in data for field in required_fields):
            return jsonify({
                'error': 'Missing required fields',
                'required': required_fields
            }), 400
        
        # Analyze transaction
        result = detector.analyze(data)
        
        return jsonify(result), 200
        
    except Exception as e:
        return jsonify({
            'error': str(e)
        }), 500

@app.route('/api/fraud/train', methods=['POST'])
def train_model():
    try:
        # Trigger model retraining
        detector.train_model()
        return jsonify({
            'message': 'Model training completed successfully'
        }), 200
    except Exception as e:
        return jsonify({
            'error': str(e)
        }), 500

if __name__ == '__main__':
    port = int(os.getenv('PORT', 8083))
    app.run(host='0.0.0.0', port=port, debug=True)