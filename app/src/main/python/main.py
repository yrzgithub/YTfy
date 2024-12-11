from pytubefix import YouTube
from youtube_search import YoutubeSearch
import requests
import urllib.parse
import json



BASE_URL = "https://youtube.com"


def getStream(url:str)->[str]:
    yt = YouTube(url)
    return yt.streams.filter(only_audio=True).get_highest_resolution(progressive=False).url


def getSuggestions(query:str)->[str]:
    search = Search(query)
    return search.completion_suggestions


def search(query,max_results=10,max_retries=10):
        encoded_search = urllib.parse.quote_plus(query)
        url = f"{BASE_URL}/results?search_query={encoded_search}"
        response = requests.get(url).text

        while "ytInitialData" not in response and max_retries>=0:
            print("Retrying...")
            response = requests.get(url).text
            max_retries -= 1

        results = parse_html(response)

        if max_results is not None and len(results) > max_results:
            return results[: max_results]

        return results


def parse_html(response):
        results = []
        start = (
            response.index("ytInitialData")
            + len("ytInitialData")
            + 3
        )
        end = response.index("};", start) + 1
        json_str = response[start:end]
        data = json.loads(json_str)

        for contents in data["contents"]["twoColumnSearchResultsRenderer"]["primaryContents"]["sectionListRenderer"]["contents"]:
            for video in contents["itemSectionRenderer"]["contents"]:
                res = {}
                if "videoRenderer" in video.keys():
                    video_data = video.get("videoRenderer", {})
                    res["id"] = video_data.get("videoId", None)
                    res["thumbnails"] = video_data.get("thumbnail", {}).get("thumbnails", [{}])[0]["url"]
                    res["title"] = video_data.get("title", {}).get("runs", [[{}]])[0].get("text", None)
                    res["channel"] = video_data.get("longBylineText", {}).get("runs", [[{}]])[0].get("text", None)
                    res["duration"] = video_data.get("lengthText", {}).get("simpleText", 0)
                    res["views"] = video_data.get("shortViewCountText", {}).get("simpleText", 0)
                    res["publish_time"] = video_data.get("publishedTimeText", {}).get("simpleText", 0)
                    results.append(res)

            if results:
                return results

        return results