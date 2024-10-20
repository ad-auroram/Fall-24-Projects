from django.shortcuts import redirect
from .models import Session

def session_middleware(next):
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
    return middleware