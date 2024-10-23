from django.urls import path
from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("users/new", views.newUser, name="newUser"),
    path("sessions/new", views.newSession, name="newSession"),
    path("sessions/destroy", views.deleteSession, name="delete"),
    path("destinations", views.destinations, name="destinations"),
    path("destinations/new", views.newDestination, name="newDestination"),
    path("destinations/<int:id>", views.editEntry, name="edit"),
    path("error", views.error, name="error")
]