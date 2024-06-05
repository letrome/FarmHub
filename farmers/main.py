import argparse

import uvicorn
from fastapi import FastAPI

from controller import router

app = FastAPI()
app.include_router(router)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="FastAPI Server")
    parser.add_argument("--host", dest="host", default="0.0.0.0", help="Host IP address")
    parser.add_argument("--port", dest="port", default=6000, help="Port number")
    args = parser.parse_args()

    uvicorn.run(app, host=args.host, port=int(args.port))
