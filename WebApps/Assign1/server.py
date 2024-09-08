
#run server
#decode requests
import socket
import datetime

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

def makeHeaders(data):
    headers = {}
    for i in range(len(data)):
        entry= data[i].replace("\r","")
        entries = entry.split(":",1)
        headers[entries[0]]=entries[1]
    return headers

#decides which endpoint to call
#router
def router(req):
    if req.uri == "/about": (about(req))
    elif req.uri == "/experience": (experience(req))
    elif req.uri == "/": (experience(req))
    elif req.uri == "/projects": (projects(req))
    elif req.uri == "/info": (info(req))
    else: print("URI not found")
#middleware
def compose(end_result_function, middleware_factory_list):
# your implementation here, remove the `pass` when you are done
    def composed(request):
        for middleware in reversed(middleware_factory_list):
            request=middleware(request)
        response = end_result_function(request)
        return response
    return composed

def logging(data):
    if isinstance(data, Request):
        print(data.method + " " + data.uri)
    elif isinstance(data, Response):
        print(data.uri + " "+ data.code + " "+ data.reason)

def findStatic(data):
    #if static file is requested, send in a response
    pass
    
def makeGoodHeaders(req, text):
    headers={"Server:" : "Cool guy server", 
             "Date:": datetime.datetime.now(), 
             "Connection:" : "close", 
             "Cache-Control:": "max-age=200"
            }  
    return headers

def error():
    print("uh oh")


# called by the router when the URI is /about
def about(req):
    file = open("templates/about.html")
    response_text = file.read()
    return response_text

# called by the router when the URI is /experience
def experience(req):
    file = open("templates/experience.html")
    response_text = file.read()
    return response_text

# called by the router when the URI is /
def index(req):
    file = open("templates/index.html")
    response_text = file.read()
    return response_text

# called by the router when the URI is /projects
def projects(req):
    file = open("templates/projects.html")
    response_text = file.read()
    return response_text

# called by the router when the URI is /info
def info(req):
    # returns an HTTP redirect response that redirects the client to `/about`. 
    # You should use 301 for the static code. Don't forget to set the `Location` header to tell the browser where to go.
    pass


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
            data = data.decode()
            print(data)
            dataList=data.strip("\r").split("\n")
            requestText=dataList[0]
            requestHeaders = makeHeaders(dataList[1:len(dataList)-2])
            requestInfo=requestText.split(" ")
            request= Request(requestInfo[0], requestInfo[1], requestInfo[2], requestText, requestHeaders)
            #testing
            print(request.uri)
            print(request.text)
            #TODO: parse the request, send through middleware and encode the response
            middleware_list=[logging, findStatic]
            text=index(dataList[1])
            res=compose(router(request),middleware_list)
            #res = "HTTP/1.1 200 Ok\nConnection: close\n\n" + text
            connection.send(bytes(res, "UTF-8"))