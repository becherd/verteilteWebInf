__author__ = 'G6'

import zmq

context = zmq.Context()
sock = context.socket(zmq.REP)
sock.bind("tcp://127.0.0.1:8000")

# Just send a pre-defined string back as soon as the
# server receives a message
while True:
    message = sock.recv()
    sock.send_string("NENENENENE")