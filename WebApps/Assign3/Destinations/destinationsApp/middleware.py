from django.shortcuts import redirect
from .models import Session


""" def session_middleware(next):
    def middleware(req):
        #find user based on session token
        needsLogin = ["destinations", "destinations/new", "destinations/<int:id>", "sessions/destroy"]
        token = req.COOKIES.get('session_token')

        if token:
            try:
                session = Session.objects.get(token=token)
                req.user = session.user
            except Session.DoesNotExist:
                req.user = None
        if req.user is None and req.path in needsLogin:
            return redirect('/sessions/new')
        res = next(req)
        return res
    return middleware """

def session_middleware(get_response):
    def middleware(req):
        # List of paths that do not require a logged-in user
        needsLogin = ["/destinations/", "/destinations/new", "/sessions/destroy"]

        # Read the session token from the cookie
        token = req.COOKIES.get('session_token')

        if token:
            try:
                # Find the session by its token
                session = Session.objects.get(token=token)
                req.user = session.user  # Attach the user to the request
            except Session.DoesNotExist:
                req.user = None
        else:
            req.user = None

        # Check if the user needs to be logged in for the requested path
        if req.user is None and req.path in needsLogin:
            return redirect('/sessions/new')  # Redirect to login if needed

        # Call the next middleware or view
        response = get_response(req)
        return response

    return middleware
