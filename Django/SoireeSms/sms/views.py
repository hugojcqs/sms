from datetime import datetime
import pprint
from json import JSONDecodeError
import hashlib

from asgiref.sync import async_to_sync
from channels.layers import get_channel_layer
from django.contrib.auth import authenticate, login, logout
from django.core.files.base import ContentFile
from django.http.response import HttpResponseServerError, JsonResponse, HttpResponse
import json
from django.contrib import messages

from django.shortcuts import render, redirect
from django.utils.html import strip_tags
from .forms import LoginForm
from .models import SMSModel, Photo
# Create your views here.
import base64
# TODO : add security (auth ?)
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings


@csrf_exempt
def submit_sms(request):
    if request.method == 'POST':
        try:
            print(request.body)
            data_string = request.body.decode('utf-8')
            data = json.loads(data_string)
            data['action'] = 'new_sms'
            data['sms'] = strip_tags(data['sms'])
            sms = SMSModel.objects.create(number = data['number'],
                                          content = strip_tags(data['sms']))
            sms.save()
            data['id'] = sms.id
            data['date_time'] = datetime.strftime(sms.date_time, '%b %d, %Y, %I:%M %p').replace(' 0', '')
            data_string = json.dumps(data)
            new_sms_notification(data_string)
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
            data = request.FILES['media'].read()
            write_image(data)

            message = {'action': 'display_image', 'image': base64.b64encode(data).decode('utf8')}

            channel_layer = get_channel_layer()
            async_to_sync(channel_layer.group_send)(
                'display',
                {
                    'type': 'websocket.send',
                    'text': json.dumps(message)
                }
            )

        except Exception as e:
            print(e)
            return JsonResponse({'status': 'ko'})
        return JsonResponse({'status': 'ok'})
    else:
        return HttpResponseServerError("Error : post request only!")


def new_sms_notification(sms):
    channel_layer = get_channel_layer()
    async_to_sync(channel_layer.group_send)(
        'client',
        {
            'type': 'websocket.send',
            'text':sms
        }
    )


def main_page(request):
    return render(request, 'main_page.html')


def logout_page(request):
    logout(request)
    return redirect('login_page')


def control_page(request):
    print(SMSModel.objects.all())
    smss = SMSModel.objects.all()
    len_smss = len(smss)
    max_obj = 50
    if len_smss < max_obj:
        max_obj = len_smss
    return render(request, 'control_page.html', context = {'smss':smss[len_smss-max_obj:len_smss]})


def login_page(request):
    login_form = LoginForm(request.POST or None)
    if login_form.is_valid():
        user = authenticate(request,
                            username=login_form.cleaned_data['username'],
                            password=login_form.cleaned_data['password'])
        if user is not None:
            login(request, user)
            return redirect('main_page')
        else:
            messages.add_message(request, messages.ERROR, 'Login error!')
            return render(request, 'login_page.html', {'form': login_form})
    return render(request, 'login_page.html', {'form': login_form})

'''

    number = models.CharField(max_length=15, null=False)
    content = models.TextField(null=False)
    date_time = models.DateTimeField(auto_now_add=True, blank=True, null=False)
    approved = models.BooleanField(default=False)

'''