from django.urls import path
from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("/new_activity", views.newActivity, name="index"),
    path("/activity/{placeholder}", views.activity, name="index"),
    path("/activity/{placeholder}/new_timelog", views.newTimelog, name="index")
]