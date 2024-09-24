from django.shortcuts import render
from django.http import HttpResponse
# Create your views here.

def index(request):
    activities = []
    #TODO: add all activities to a list
    return render(request, "Tracker/index.html")

def newActivity(request):
    return render(request, "Tracker/new_activity.html")

def activity(request):
    #activity = get the activity by requested id
    return render(request, "Tracker/activity.html")

def newTimelog(request):
    return render(request, "Tracker/new_time.html")