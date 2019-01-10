from __future__ import print_function
import socket
import os

HOST = '127.0.0.1'  
PORT = 65432        

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    conn, addr = s.accept()
    with conn:
        orgpath=os.getcwd()
        print('Connected by client')
        while True:
            data = conn.recv(1024)
            if data==b'list':
                lists=''
                files = os.listdir()
                for name in files:
                  lists=lists+name+'\n'
                  print(name)
                print(bytes(lists, 'utf-8'))  
                conn.sendall(bytes(lists, 'utf-8'))
                
            elif data==b'cd':
                p=conn.recv(1024)
                p=p.decode('utf-8')
                if p=="y":
                    os.chdir(orgpath)
                    conn.sendall(bytes("dir changed exist", 'utf-8'))
                elif p=="n":
                    a=conn.recv(1024)
                    a=a.decode('utf-8')
                    print(a)
                    if os.path.isdir(a):
                        a='./'+a
                        os.chdir(a)
                        testpar=os.getcwd()
                        print(testpar)
                        conn.sendall(bytes("dir changed exist", 'utf-8'))
                    else:
                        conn.sendall(b'0')
                        

            elif data==b'ret':
                f=conn.recv(1024)
                f=f.decode('utf-8')
                print(f)
                if os.path.exists(f):
                    f='./'+f
                    outfile=open(f,"r")
                    odata=""""""
                    odata=outfile.read()
                    print(odata)
                    conn.sendall(bytes(odata, 'utf-8'))
                else:    
                    conn.sendall(b'0')         
                
                
            
