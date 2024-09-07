
#run server
#decode requests
import socket

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind(("127.0.0.1", 8000))
    s.listen()
    print("listening on port 8000")

    while True:
        connection, addr = s.accept()
        with connection:
            data = connection.recv(8192)
            if not data:
                connection.close()
                continue

            #TODO: parse the request, send through middleware and encode the response
            res = "HTTP/1.1 200 Ok\nConnection: close\n\n<h1>Hello, world!</h1>"

            connection.send(bytes(res, "UTF-8"))

class Request:
    def __init__(
        self,
        method, #string
        uri, #string
        version, #string
        text, #string
        headers, #dict, the keys are the header names and values are the header values
    ):
        self.method = method
        self.uri = uri
        self.version = version
        self.text = text
        self.headers = headers

class Response:
    def __init__(
            self,
            version, #string
            code, #number
            reason, #string
            headers, #dict, the keys are the header names and values are the header values 
            text, #string
    ):
        self.version = version
        self.code = code
        self.reason = reason
        self.headers = headers
        self.text = text


#router
#decides which endpoint to call
#middleware

#endpoints (correspond to templates)

# called by the router when the URI is /projects
def projects(req):
    response_text = open("/templates/projects.html")
    return #<create a response object with the appropriate headers and text>