from django.shortcuts import render, redirect
from django.http import HttpResponse
from datetime import datetime
from .models import Activity, TimeLog
# Create your views here.

def index(request):
    activities = Activity.objects.all()
    return render(request, "Tracker/index.html", {'activities': activities})

def newActivity(request):
    if request.method == "POST":
        params = request.POST
        activity = Activity(
            name=params.get("name")
        )
        activity.save()
        start_time = params.get("start_time")
        end_time = params.get("end_time")
        if start_time and end_time:  # Ensure both times are provided
            time_log = TimeLog(
                start_time=start_time,
                end_time=end_time,
                activity=activity
            )
            time_log.save()

        return redirect("index")

    return render(request, "Tracker/new_activity.html")

def activity(request, activity_id):
    activity = Activity.objects.get(id=activity_id)
    times = activity.timelog_set.all()
    return render(request, "Tracker/activity.html", {'activity': activity, "times":times})

def newTimelog(request, activity_id):
    if request.method == "POST":
        params = request.POST

        start_time = datetime.strptime(params.get("start_time"), '%Y-%m-%dT%H:%M')
        end_time = datetime.strptime(params.get("end_time"), '%Y-%m-%dT%H:%M')
        
        activity = Activity.objects.get(id=activity_id)

        time_log = TimeLog(
            start_time=start_time,
            end_time=end_time,
            activity=activity
        )
        time_log.save()

        return redirect("activity", activity_id=activity_id)

    return render(request, "Tracker/new_time.html", {'activity_id': activity_id})