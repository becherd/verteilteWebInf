#include <iostream>
#include <string>
#include "zmq.hpp"
using namespace std;

int input_exit(char *str);

int main() {
	char* sendline;
	//Socket und Context erstellen
	zmq::context_t ctx(1);
	zmq::socket_t socket(ctx, 0);	//ZMQ_PUB
	//verbinden
	socket.connect ("tcp://127.0.0.1:5678");
	//msg initiailisieren
	zmq::message_t msg(1000);
	zmq::message_t reply(1000);
	memset (msg.data(), 0, 1000);

	cin >> sendline;

	//schreiben
   while (input_exit(sendline) == 0)
	{
	   memcpy ((void *) msg.data(), sendline, sizeof(sendline));

	   socket.send(msg);
		socket.recv(&reply);
		cout << "Empfangen: " << (char *)reply.data() <<std::endl;
		cin >> sendline;
   }
	return 0;
}

//wurde die Eingabe mittels exit beendet?
int input_exit(char *str){
	if(*(str)=='e' && *(str+1)=='x' && *(str+2)=='i' && *(str+3)=='t')
		return 1;
	return 0;
}
