import asyncio
import json

from asgiref.sync import async_to_sync
from django.contrib.auth import get_user_model
from channels.consumer import AsyncConsumer
from channels.db import database_sync_to_async
from .models import *


class MessageControlConsumer(AsyncConsumer):
    async def websocket_connect(self, event):
        print("connnected", event)
        await self.channel_layer.group_add(
            'client',
            self.channel_name
        )
        await self.send({'type': 'websocket.accept'})

    async def websocket_send(self, message):
        await self.send(message)

    async def websocket_receive(self, event):
        print("receive", event)
        
    async def websocket_disconnect(self, event):
        print("disconnect", event)
        async_to_sync(self.group_name)('client')