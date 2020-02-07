import requests
import json

url = "http://localhost:8000/submit_image/"

files = {'media': open('test.jpg', 'rb')}
requests.post(url, files=files)
