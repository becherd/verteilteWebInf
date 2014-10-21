__author__ = 'G6'

import zmq
import sys

context = zmq.Context()
sock = context.socket(zmq.REQ)
sock.connect("tcp://127.0.0.1:8000")

# Send a defined using the socket
sock.send(" ".join(sys.argv[1:]))
print (sock.recv())