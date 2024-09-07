
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
            data = data.decode()
            print(data)
            dataList=data.split(" ")
            #request= Request(dataList[0], dataList[1], dataList[2], "buh", "e")
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

            #decides which endpoint to call
            #router
            def router(req):
                middleware=[]
                if req.uri == "/about": compose(about(req), middleware)
                elif req.uri == "/experience": (experience(req), middleware)
                elif req.uri == "/index": (experience(req), middleware)
                elif req.uri == "/projects": (projects(req), middleware)
                elif req.uri == "/info": (info(req), middleware)
                else: print("URI not found")
            #middleware
            def compose(end_result_function, middleware_factory_list):
            # your implementation here, remove the `pass` when you are done
                pass

            
            #endpoints (correspond to templates)

            # called by the router when the URI is /about
            def about(req):
                file = open("/templates/about.html")
                response_text = file.read()
                response = Response("X","Y","Z","A", response_text)
                return response
            
            # called by the router when the URI is /experience
            def experience(req):
                file = open("/templates/experience.html")
                response_text = file.read()
                response = Response("X","Y","Z","A", response_text)
                return response
            
            # called by the router when the URI is /
            def index(req):
                file = open("/templates/index.html")
                response_text = file.read()
                response = Response("X","Y","Z","A", response_text)
                return response

            # called by the router when the URI is /projects
            def projects(req):
                file = open("/templates/projects.html")
                response_text = file.read()
                response = Response("X","Y","Z","A", response_text)
                return response
            
            # called by the router when the URI is /info
            def info(req):
                # returns an HTTP redirect response that redirects the client to `/about`. 
                # You should use 301 for the static code. Don't forget to set the `Location` header to tell the browser where to go.
                pass