from django.shortcuts import render, redirect
from .models import User, Destination, Session
# Create your views here.
def index(request):
    #activities = Activity.objects.all()
    return render(request, "Destinations/index.html")


def newUser(request):
    if request.method == "POST":
        params = request.POST
        user = User(
            name = params.get("name"),
            email = params.get("email"),
            password_hash = params.get("password"), #TODO: FIGURE OUT PASSWORD HASHING
        )
        user.save()
        #logged in now, make cookie for that
        return redirect("destinations")
    return render(request, "Destinations/new_user.html")

def newSession(request):
    if request.method == "POST":
        params = request.POST
        #check if user in table
        #check if password is legit
        #make session cookie, we're logged in now
        return redirect("destinations")
    return render(request, "Destinations/new_session.html")

def destinations(request):
    return render(request, "Destinations/destinations.html")

def newDestination(request):
    return render(request, "Destinations/new_destination")

def editEntry(request):
    return render(request, "Destination/edit_destination")