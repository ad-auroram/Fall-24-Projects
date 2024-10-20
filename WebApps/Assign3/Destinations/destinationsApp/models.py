from django.db import models

# Create your models here.
class User(models.Model):
    name = models.TextField()
    email = models.TextField(unique=True)
    password_hash =models.TextField()

class Session(models.Model):
    token = models.TextField(unique=True)
    user = models.OneToOneField('User', on_delete=models.CASCADE)

class Destination(models.Model):
    name= models.TextField()
    review = models.TextField(blank=True)
    rating = models.IntegerField(default=3)
    share_publically = models.BooleanField()
    user =models.ForeignKey(User, on_delete=models.CASCADE)