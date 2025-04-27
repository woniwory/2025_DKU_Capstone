import asyncio
import websockets

connected_clients = set()

async def echo(websocket, path):
    print("클라이언트 연결됨")
    connected_clients.add(websocket)
    try:
        async for message in websocket:
            print(f"받은 메시지: {message}")
            await websocket.send(f"서버 응답: {message}")
    except websockets.exceptions.ConnectionClosed:
        print("클라이언트 연결 종료")
    finally:
        connected_clients.remove(websocket)

start_server = websockets.serve(echo, "localhost", 8765)

print("WebSocket 서버 시작 (ws://localhost:8765)")
asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()
