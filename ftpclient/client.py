
import socket

HOST = '127.0.0.1'  
PORT = 65432        

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    while True:
            cmd=input("please enter your command\ncd / ret / list\n")
            if cmd=="list":
                print("files in current path:")
                s.sendall(b'list')
                data = s.recv(1024)
                data=data.decode('utf-8')
                print(data)
                    
                
            elif cmd=="cd":
                s.sendall(b'cd')
                ans=input("dou you want to go to parent path?y/n\n")
                if ans=="n":
                    s.sendall(bytes("n" , 'utf-8'))
                    p=input("please enter your path\n")
                    s.sendall(bytes(p,'utf-8')) 
                elif ans=="y":
                    s.sendall(bytes("y" , 'utf-8'))
                else:
                    print("unexpected command")
                res = s.recv(1024)
                if res==b'0':
                    print("path doesn't exist")
                else:
                    print("path changed")

            elif cmd=="ret":
                
                s.sendall(b'ret')
                f=input("please enter file name\n")
                s.sendall(bytes(f,'utf-8'))
                indata=s.recv(1024)
                if indata==b'0':
                    print("file doesn't exist")
                else:    
                    indata=indata.decode('utf-8')
                    with open(f,"w") as retfile:
                        retfile.writelines(indata)
                        print("file downloaded")
                        


            else:
                print("unexcepted command")
