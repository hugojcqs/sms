import pprint
from json import JSONDecodeError
import hashlib

from django.core.files.base import ContentFile
from django.http.response import HttpResponseServerError, JsonResponse, HttpResponse
import json
from .models import SMSModel, Photo
# Create your views here.

# TODO : add security (auth ?)
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings

@csrf_exempt
def submit_sms(request):
    if request.method == 'POST':
        try:
            print(request.body)
            data = json.loads(request.body.decode('utf-8'))
            sms = SMSModel.objects.create(number = data['number'],
                                          content = data['sms'])
            sms.save()
        except JSONDecodeError:
            return JsonResponse({'status':'ko'})
        return JsonResponse({'status':'ok'})
    else:
        return HttpResponseServerError("Error : post request only!")


def write_image(data):
    m = hashlib.sha3_256()
    m.update(data)
    file_hash = m.hexdigest()
    if not Photo.objects.filter(hash=file_hash).exists():
        image = Photo()
        image.hash = file_hash
        image.photo_webcam.save("%s.jpg" % file_hash, ContentFile(data), save=True)
    else:
        print('File already exists')

@csrf_exempt
def submit_image(request):
    if request.method == 'POST':
        try:
            write_image(request.FILES['media'].read())
        except Exception as e:
            print(e)
            return JsonResponse({'status': 'ko'})
        return JsonResponse({'status': 'ok'})
    else:
        return HttpResponseServerError("Error : post request only!")


def main_page(request):
    return HttpResponse("Page principale")



'''

    number = models.CharField(max_length=15, null=False)
    content = models.TextField(null=False)
    date_time = models.DateTimeField(auto_now_add=True, blank=True, null=False)
    approved = models.BooleanField(default=False)

'''