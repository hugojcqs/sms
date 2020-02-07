from django.db import models


# Create your models here.
class SMSModel(models.Model):
    number = models.CharField(max_length=15, null=False)
    content = models.TextField(null=False)
    date_time = models.DateTimeField(auto_now_add=True, blank=True, null=False)
    approved = models.BooleanField(default=False)
    # displayed = models.BooleanField(default=False)

    def __str__(self):
        return self.number + self.content


class Photo(models.Model):
    photo_webcam = models.ImageField(upload_to='image', blank=True, null=True)
    hash = models.TextField(null=True)
    # displayed = models.BooleanField(default=False)

    def __str__(self):
        return self.hash