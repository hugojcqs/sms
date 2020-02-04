from django.shortcuts import render
from django.http.response import HttpResponseServerError, JsonResponse, HttpResponse
import json
# Create your views here.

# TODO : add security (auth ?)
def submit_sms(request):
    if request.method == 'POST':
        body_unicode = request.body.decode('utf-8')
        body = json.loads(body_unicode)
        content = body['content']
        print("New sms received!", content)
        return JsonResponse({'status':'ok'})  # TODO : add error notification with JSON
    else:
        return HttpResponseServerError("Error : post request only!")

def main_page(request):
    return HttpResponse("Page principale")