from json import JSONDecodeError
from facebook_scraper import *
import traceback
from pathlib import Path

cookiesPath = "./cookies.txt"
Pages = 3


def write_posts(page, posts):
    p = Path('./' + page)
    p.mkdir(exist_ok=True)
    t = int(time.time())
    ouput_file_name = './' + page + "/posts_" + str(t) + ".json"
    with open(ouput_file_name, 'w', encoding='utf-8') as f:
        f.write(json.dumps(posts, ensure_ascii=False).encode('utf8').decode())


class custom_post:
    def __init__(self, post_id, post_text, shared_text, reaction_count, username, reactions, comments):
        self.post_id = post_id
        self.post_text = post_text
        self.shared_text = shared_text
        self.reaction_count = reaction_count
        self.username = username
        self.reactions = reactions
        self.comments = comments


class custom_comment:
    def __init__(self, text, reactions):
        self.text = text
        self.reactions = reactions


def get_posts_with_comments_for_page(page):
    posts = []
    cnt = 1
    options = {"comments": True, "reactors": False, "progress": True}
    try:
        for post in get_posts(account=page, pages=Pages, extra_info=True, cookies=cookiesPath,
                              options=options):
            try:
                print("Progress post n: " + str(cnt))
                cnt = cnt + 1
                comments = []
                for comment in post['comments_full']:
                    comments.append(custom_comment(comment['comment_text'], comment['comment_reactions']).__dict__)
                posts.append(
                    custom_post(post['post_id'], post['post_text'], post['shared_text'], post['reaction_count'],
                                post['username'], post['reactions'],
                                comments).__dict__)
            except Exception as inst:
                print(type(inst))
                print(inst.args)
                print(inst)
            except JSONDecodeError as jsonError:
                print(str(jsonError))
    except exceptions.TemporarilyBanned:
        print("Temporarily banned, sleeping for 10m")
        time.sleep(600)
    write_posts(page, posts)


def create_paths():
    p = Path('./logs')
    p.mkdir(exist_ok=True)


if __name__ == '__main__':
    create_paths()
    with open("logs/logs.txt", "a") as log:
        try:
            pages = ["leparisien", "BFMTV", "Ligue1UberEats", "equipedefrance", "airfrance", "FRANCE24",
                     "100047556278333",
                     "CGTNFrancais", "Chefclub.tv"]
            for page in pages:
                get_posts_with_comments_for_page(page)
                time.sleep(600)  # 10min
            time.sleep(86400)  # 24h
        except Exception:
            log.write("###### " + str(datetime.now()) + " ######\n")
            traceback.print_exc(file=log)
            traceback.print_exc(file=sys.stdout)
            pass
