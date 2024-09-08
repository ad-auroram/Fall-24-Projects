
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

    def to_string(self):
        headers = '\r\n'.join(f"{key}: {value}" for key, value in self.headers.items())
        
        response = (
            f"{self.version} {self.code} {self.reason}\r\n"
            f"{headers}\r\n"
            f"{self.text}"
        )
        return response

def makeRequestHeaders(data):
    headers = {}
    for i in range(len(data)):
        entry= data[i].replace("\r","")
        entries = entry.split(":",1)
        headers[entries[0]]=entries[1]
    return headers

#decides which endpoint to call
#router
def router(req):
    if req.uri == "/about":
        return about(req)
    elif req.uri == "/experience":
        return experience(req)
    elif req.uri == "/":
        return index(req)
    elif req.uri == "/projects":
        return projects(req)
    elif req.uri == "/info":
        return info(req)
    else:
        headers = {"Content-Type": "text/plain", "Connection": "close"}
        return Response("HTTP/1.1", 404, "Not Found", headers, "Page not found")

#middleware
def compose(end_result_function, middleware_factory_list):
    def composed_function(request):
        for middleware in reversed(middleware_factory_list):
            request = middleware(request)
            if isinstance(request, Response):
                break
        if not isinstance(request, Response):
            response = end_result_function(request)
        else:
            response = request
        return response
    return composed_function

def logging(data):
    if isinstance(data, Request):
        print("Request:" + data.method + " " + data.uri)
        return data
    elif isinstance(data, Response):
        print("Response:"+ data.uri + " "+ data.code + " "+ data.reason)
        return data

def findStatic(data):
    if data.uri.startswith("/static/"):
        file_path = data.uri[len("/static/"):]
        full_path = f"static/{file_path}"
        try:
            with open(full_path, "r") as file:
                response_text = file.read()
            content_type = "text/html"
            if file_path.endswith(".js"):
                content_type = "text/javascript"
            elif file_path.endswith(".css"):
                content_type = "text/css"
            headers=makeGoodHeaders(data)
            headers = {
                "Content-Type": content_type,
                "Content-Length": str(len(response_text)),
            }
            return Response("HTTP/1.1", 200, "OK", headers, response_text)
        
        except FileNotFoundError:
            headers = {
                "Content-Type": "text/plain",
                "Connection": "close"
            }
            return Response("HTTP/1.1", 404, "Not Found", headers, "File not found")
    return data
    
def makeGoodHeaders(req):
    headers={"Server:" : "Cool guy server", 
             "Date:": datetime.datetime.now(), 
             "Connection:" : "close", 
             "Cache-Control:": "max-age=200"
            }  
    return headers
def error():
    print("uh oh")


#endpoints
# called by the router when the URI is /about
def about(req):
    file = open("templates/about.html")
    response_text = file.read()
    return makeResponse(req, "200", response_text)
# called by the router when the URI is /experience
def experience(req):
    file = open("templates/experience.html")
    response_text = file.read()
    return makeResponse(req, "200", response_text)
# called by the router when the URI is /
def index(req):
    file = open("templates/index.html")
    response_text = file.read()
    print("hello there")
    return makeResponse(req, "200", response_text)
# called by the router when the URI is /projects
def projects(req):
    file = open("templates/projects.html")
    response_text = file.read()
    return makeResponse(req, "200", response_text)
# called by the router when the URI is /info
def info(req):
    # returns an HTTP redirect response that redirects the client to `/about`. 
    # You should use 301 for the static code. Don't forget to set the `Location` header to tell the browser where to go.
    return makeResponse(req, "301", "placeholder")


def makeResponse(req, code, text):
    headers = makeGoodHeaders(req)
    if code == "200":
        response = Response("HTTP/1.1", code, "OK", headers, text)
    elif code == "301":
        headers = {"Location": "/about"}  # Redirects to "/about"
        response = Response("HTTP/1.1", code, "Moved Permanently", headers, "")
    return response


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
            requestHeaders = makeRequestHeaders(dataList[1:len(dataList)-2])
            requestInfo=requestText.split(" ")
            request= Request(requestInfo[0], requestInfo[1], requestInfo[2], requestText, requestHeaders)
            #testing
            print(request.uri)
            print(request.text)
            #TODO: parse the request, send through middleware and encode the response
            middleware_list = [findStatic, logging]
            composed_function = compose(router, middleware_list)
            response = composed_function(request)
            
            # Convert response to string and send
            res = response.to_string()
            connection.sendall(res.encode("UTF-8"))
            #res = "HTTP/1.1 200 Ok\nConnection: close\n\n" + text