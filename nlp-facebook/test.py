from facebook_scraper import *
from pathlib import Path

if __name__ == '__main__':
    p = Path('./logs')
    p.mkdir(exist_ok=True)
    with open("logs/logs.txt", "a") as log:
        try:
            raise Exception("Sorry, no numbers below zero")
        except Exception:
            log.write("###### " + str(datetime.now()) + " ######\n")
            traceback.print_exc(file=log)
            pass
