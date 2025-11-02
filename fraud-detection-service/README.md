# Fraud Detection Service

AI/ML powered fraud detection microservice.

## Setup
```bash
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
```

## Run
```bash
python app.py
```

## API Endpoints

### Analyze Transaction
```bash
POST http://localhost:8083/api/fraud/analyze
{
  "amount": 1500.0,
  "merchantId": "merchant-123",
  "timestamp": "2025-11-02T20:30:00Z"
}
```