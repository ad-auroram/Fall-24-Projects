from django.shortcuts import render, redirect
from .models import User, Destination, Session
# Create your views here.
def index(request):
    #make a list of 5 recents and return with render
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
    #make a list of destination that belong to that user
    #return it with the render and add a loop to the page
    return render(request, "Destinations/destinations.html")

def newDestination(request):
    if request.method == "POST":
        params = request.POST
        destination = Destination(
            name= params.get("destination"),
            review = params.get("review"),
            rating = params.get("rating"),
            share_publically = params.get("public"),
            #get the user from the form
        )
        destination.save()
        return redirect("destinations")
    return render(request, "Destinations/new_destination")

def editEntry(request):
    #figure out how to autofill the form
    return render(request, "Destination/edit_destination")