from django.db import models
from datetime import timedelta

# Create your models here.
class Activity(models.Model):
    id = models.IntegerField
    name = models.CharField

class TimeLog(models.Model):
    id = models.IntegerField
    start_time = models.DateTimeField
    end_time = models.DateTimeField
    activity =models.ForeignKey(Activity, on_delete=models.CASCADE)