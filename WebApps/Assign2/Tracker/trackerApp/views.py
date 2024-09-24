from django.shortcuts import render
from django.http import HttpResponse
# Create your views here.

def index(request):
    activities = []

    return render(request, "Tracker/index.html")

def newActivity(request):

    return render(request, "Tracker/new_activity.html")

def activity(request):

    return render(request, "Tracker/activity.html")

def newTimelog(request):

    return render(request, "Tracker/new_time.html")