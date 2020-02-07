import asyncio
import json

from asgiref.sync import async_to_sync
from django.contrib.auth import get_user_model
from channels.consumer import AsyncConsumer
from channels.db import database_sync_to_async
from .models import *


class MessageControlConsumer(AsyncConsumer):
    async def websocket_connect(self, event):

        await self.channel_layer.group_add(
            'client',
            self.channel_name
        )
        self.user = self.scope["user"]
        if self.user.is_authenticated:
            await self.send({'type': 'websocket.accept'})
        else:
            await self.send({'type': 'websocket.disconnect'})

    async def websocket_send(self, message):
        await self.send(message)

    async def websocket_receive(self, event):

        try:
            received_data = json.loads(event['text'])
            id = received_data['id']
            sms = await self.allow_sms(id)
            await self.channel_layer.group_send('client',
                                  {
                                      'type': 'websocket.send',
                                      'text':json.dumps({'action':'allow', 'id':id})
                                  })
            number = sms.number[:-4] + '****'
            message = sms.content

            await self.channel_layer.group_send('display',
                                                {
                                                    'type': 'websocket.send',
                                                    'text': json.dumps({'action': 'display', 'number': number,
                                                                        'message': message})
                                                })
        except Exception as e:
            print(e)

    async def websocket_disconnect(self, event):
        print("disconnect", event)

    @database_sync_to_async
    def allow_sms(self, id):
        sms = SMSModel.objects.get(pk=id)
        sms.approved = True
        sms.save()
        return sms