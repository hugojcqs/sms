import requests
import json

datas = {'sms':'<script>alert("coucou")</script>', 'number':'0478121113'}

url = 'http://localhost:8000/submit_sms/'
requests.post(url, data=json.dumps(datas))

