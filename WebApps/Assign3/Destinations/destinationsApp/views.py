from django.shortcuts import render, redirect
from .models import User, Destination, Session
from django.contrib.auth.hashers import make_password, check_password
# Create your views here.
def index(request):
    #make a list of 5 recents and return with render
    destinations = Destination.objects.order_by('-id')[:5]
    return render(request, "Destinations/index.html",{'destinations': destinations})

def number(s):
    return any(i.isdigit() for i in s)

def newUser(request):
    if request.method == "POST":
        params = request.POST
        password = params.get("password")
        user = User(
            name = params.get("name"),
            email = params.get("email"),
            password_hash = make_password(password),
        )
        if user.name and user.email and user.password_hash:
            if "@" not in user.email:
                return redirect("error")
            elif len(user.password_hash)< 8:
                return redirect("error")
            elif not number(user.password_hash):
                return redirect("error")
            else:        
                user.save()
            #logged in now, make cookie for that
            return redirect("destinations")
        else:
            return redirect("error")
    return render(request, "Destinations/new_user.html")

def newSession(request):
    if request.method == "POST":
        params = request.POST
        #check if user in table
        #check if password is legit
        #make session cookie, we're logged in now
        email=params.get("email")
        user= User.objects.get(email=email)
        password = params.get("password")
        isGood = check_password(password, user.password_hash)
        if isGood:
            return redirect("destinations")
        else:
            return redirect("index")
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
    return render(request, "Destinations/new_destination.html")

def editEntry(request):
    #figure out how to autofill the form
    return render(request, "Destinations/edit_destination.html")

def deleteSession(request):
    #delete the session token and redirect to index
    return redirect("index")

def error(request):
    return render(request, "Destinations/error.html")
