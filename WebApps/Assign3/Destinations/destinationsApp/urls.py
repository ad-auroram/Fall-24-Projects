from django.urls import path
from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("users/new", views.newUser, name="newUser"),
    path("sessions/new", views.newSession, name="newSession"),
    path("users", views.users, name="users"),
    path("sessions", views.sessions, name="sessions"),
    path("sessions/destroy", views.index, name="delete"),
    path("destinations", views.index, name="destinations"),
    path("destinations/new", views.index, name="newDestination"),
    path("destinations/<int:id>", views.index, name="destinationId"),
    path("destinations/<int: id>/destroy", views.index, name="index"),

]