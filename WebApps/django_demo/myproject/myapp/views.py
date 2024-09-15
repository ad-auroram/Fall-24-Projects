from django.shortcuts import render
from django.http import HttpResponse
# Create your views here.

def index(request):
    return render(request, "django_demo/index.html")

def passwords(request):
    return HttpResponse("TEST")