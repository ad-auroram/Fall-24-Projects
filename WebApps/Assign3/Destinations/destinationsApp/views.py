from django.shortcuts import render, redirect
from .models import User, Destination, Session
from django.contrib.auth.hashers import make_password, check_password
import secrets

# Create your views here.
def index(request):
    #make a list of 5 recents and return with render
    token = request.COOKIES.get('session_token')
    if token:
        links = [
            {"name": "Home", "url": "/"},
            {"name":"Profile", "url":"destinations"},
            {"name":"Log Out","url": "/sessions/destroy"}
            ]
    else:
        links = [
            {"name": "Log In", "url": "sessions/new"},
            {"name": "Create Account", "url": "users/new"},
        ]
    destinations = Destination.objects.filter(share_publically=True).order_by('-id')[:5]
    return render(request, "Destinations/index.html",{"links":links, 'destinations': destinations})

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
                return logIn(user)
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
            #make token
            return logIn(user)
        else:
            return redirect("index")
    return render(request, "Destinations/new_session.html")




def destinations(request):
    #make a list of destination that belong to that user
    #return it with the render and add a loop to the page
    token = request.COOKIES.get('session_token')
    session = Session.objects.get(token=token)
    posts = Destination.objects.filter(user=session.user)
    return render(request, "Destinations/destinations.html", {'posts': posts})


def newDestination(request):
    if request.method == "POST":
        params = request.POST
        print(params)
        #get session from cookie
        token = request.COOKIES.get('session_token')
        session = Session.objects.get(token=token)
                
        destination = Destination(
            name= params.get("name"),
            review = params.get("review"),
            rating = int(params.get("rating")),
            share_publically = params.get("public")== "True",
            user = session.user
        )
        destination.save()
        return redirect("destinations")
    return render(request, "Destinations/new_destination.html")


def editEntry(request):
    #figure out how to autofill the form
    return render(request, "Destinations/edit_destination.html")


def deleteSession(request):
    token = request.COOKIES.get('session_token')
    if token:
        Session.objects.filter(token=token).delete()
    response = redirect("index")
    response.delete_cookie('session_token')
    return response

#utils
def logIn(user):
    existing_session = Session.objects.filter(user=user).first()
    if existing_session:
        existing_session.delete()

    token = secrets.token_hex(32)
    session = Session.objects.create(token=token, user=user)
    session.save()
    response= redirect("destinations")
    response.set_cookie("session_token", token)
    return response
    
def number(s):
    return any(i.isdigit() for i in s)

def error(request):
    return render(request, "Destinations/error.html")
