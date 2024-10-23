from django.shortcuts import redirect
from .models import Session


def session_middleware(get_response):
    def middleware(req):
        needsLogin = ["/destinations/", "/destinations/new", "/sessions/destroy"]
        token = req.COOKIES.get('session_token')
        if token:
            try:
                session = Session.objects.get(token=token)
                req.user = session.user
            except Session.DoesNotExist:
                req.user = None
        else:
            req.user = None
        if req.user is None and req.path in needsLogin:
            return redirect('/sessions/new')
        response = get_response(req)
        return response
    return middleware
