#include "zmq.hpp"
#include <iostream>
#include <string>
using namespace std;

// c++ -o server server.cpp -lzmq

int main() {
	//Socket und Context erstellen
	zmq::context_t ctx(1);
	zmq::socket_t socket(ctx, 0);	//ZMQ_PUB
	//Verbindung erstellen
	socket.bind("tcp://*:5678");

	//senden/empfangen
	while (1) {
		zmq::message_t request;

		socket.recv(&request);
		std::cout << (char*)request.data() <<std::endl;

		zmq::message_t reply(1000);
		memcpy((void *) reply.data(), request.data(), 1000);
		socket.send(reply);
	}
	return 0;
}
