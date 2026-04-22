**Module:** 5COSC022W Client server architecture

**Name:** Ben Dias-Broughton

**ID:** W2094946

**Q1.1:**

Basically, your JAX-RS Resource classes follow
a person request cycle, meaning a brand new
instance is created every single time a request
comes in and is thrown away immediately after the
response is sent. Since these objects don't stick
around, they aren't a single thing, so you can't just
store your data in a regular list inside the class
or it would vanish the moment the request ends.
To keep your data alive across different requests, you
have to use a separate, long term DataStore with
static variables. Because your server handles many
people at once, you use a ConcurrentHashMap
to stop certain conditions which is just a fancy
way of making sure the data doesn't get scrambled
if two people try to update a room at the exact
same second.

**Q1.2:**

Think of Hypermedia (or HATEOAS) like giving a
developer a GPS instead of a map. Instead of just
sending back raw data, the server includes actual
links in the response that tell the user exactly
what they can do next, like how to view a specific
sensor or update a room. It’s considered the
"gold standard" of RESTful design because the API
becomes self discovering and the server basically
guides the client through the workflow in real time.
For a developer, this is a lifesaver compared to
static documentation. In a regular setup, if a URL
changes, the developer has to manually check a PDF
and rewrite their code. With Hypermedia, the client
just follows the links the server provides dynamically.
This makes the whole system way more flexible
and stops the client’s code from breaking everytime
the backend gets a small update.

**Q2.1:**

Deciding between sending back just IDs or the 
room objects is basically a tradeoff between speed
and convenience. If you only send IDs, the response
is tiny, which is great for network bandwidth.
However, it makes client side processing a
nightmare because the developer has to make a
separate API call for every single room just to
see a name or capacity. On the flip side, sending
full objects gives the client everything they need 
in one go, which is way more user friendly.
The only risk is that if it grows to have
thousands of rooms, that one request
could eventually become a massive, bloated file
that slows everything down.

**Q2.2:**

Yeah, my DELETE operation is definitely idempotent.
In plain English, that just means if a client
accidentally hits "delete" on the same room five
times in a row, the end result on the server is
exactly the same as if they’d only hit it once
the room is gone.
What actually happens is the very first time the
request goes through, the server finds the room
and wipes it out. If the client sends that exact
same request again, the server looks for the
room, sees it’s already missing, and basically
says there is nothing to do there. Even if the
second response is a 404 Not Found instead of a
success code, the final state of the database
hasn't changed. This is a huge safety net because
it stops duplicate requests from breaking anything
or causing weird data glitches.

**Q3.1:**

Basically, by using @Consumes(MediaType.APPLICATION_JSON), I’m
telling the server to only open the door for JSON
data. If a client tries to sneak in something
else like text/plain or application/xml JAX-RS
sees the mismatch and immediately blocks the
request. Instead of letting the bad data break
my code, JAX-RS handles the situation
automatically by sending back an HTTP 415
Unsupported Media Type error. This is a huge help
because it tells the developer which prevents
the server from crashing or trying to process
a format it doesn't understand.

**Q3.2:**

Think of the URL path as the address for the
actual object, while the query parameter
is just a way to filter what you see at that
address. If you put the sensor type directly
in the path, like /sensors/type/CO2, you're
basically creating a rigid, permanent folder
for every single category, which makes the API
structure really messy as you add more options.
Using @QueryParam is much better because it
keeps the URL clean and logical. It tells the
developer that /sensors is the main
collection, and the ?type=CO2 part is just
a temporary filter you're applying to it.
This approach is way superior for searching
because it lets you easily stack multiple
filters like type and status without having
to build a long confusing URL path for every
combination.

**Q4.1:**

Using the Sub-Resource Locator pattern is all
about keeping the code from becoming a giant,
unreadable mess. If you tried to shove every
single path like sensors, rooms, and years of
readings into one massive controller class,
it would be a nightmare to maintain. By
delegating the logic to separate classes, you
make the API modular. The SensorResource
only has to worry about basic sensor data,
and it just hands off the heavy lifting for
historical logs to a dedicated
SensorReadingResource. It keeps the project
organized and much easier to scale.

**Q5.1:**

Using a 404 is usually for when the actual URL
or address you’re hitting doesn't exist.
But when a developer sends a perfectly good
JSON formatted request to a real endpoint and
the only problem is that an ID inside that
data (like a roomId) is missing from the database
a 422 Unprocessable Entity is way more accurate.
It makes debugging much faster because they know
the URL is right, but the content is wrong.

**Q5.2:**

Dumping a raw Java stack trace is basically
handing your server to a hacker. Instead
of a clean error message, they see the exact
versions of the libraries you’re using, your
class names, and the specific line of code that
crashed.
If an attacker spots an outdated library in that
trace, they can easily look up known
vulnerabilities to exploit. It also leaks your
file directory structure, which helps them figure
out exactly how your server is organized behind
the scenes. It’s much safer to use a
safety net that catches these errors and just
sends back an Error so you aren't giving away
any secrets.

**Q5.3:**

Manually typing Logger.info() into every
single method is a waste of time and makes
your code look like a mess. If you do it that
way, you're just bloating your files with the
same repetitive lines over and over again.
Using a filter is way smarter. You write the
logging logic once, and it automatically catches
every request and response across the whole API.
It keeps your actual code clean so you can focus
on the important stuff. Plus, if you ever want
to change how the logs look like, adding a
timestamp or a specific ID you just edit one
file instead of hunting through fifty different
methods to update them all.

**Overview:**

This is a RESTful API built with JAX-RS for
managing rooms and sensors across a university
campus. It handles everything from tracking CO2
levels to managing which sensors belong in which
rooms.

**How to build and run it:**

Getting this up and running is pretty straightforward as long as you have Maven and Java installed.

Build the project:
Run mvn clean install in the project root to grab
the dependencies and build the .war file.

Launch the server:
Run mvn jetty:run.

Access the API:
The base URL should be http://localhost:8080/api/v1/

**Sample curl commands:**

1. curl GET http://localhost:8080/api/v1/

2. curl POST http://localhost:8080/api/v1/rooms 
   {
   "id": "LIB-101",
   "name": "Library",
   "capacity": 11
   }
3. curl POST http://localhost:8080/api/v1/rooms/1/sensors
   {
   "id": "TEMP-54",
   "type": "Temperature",
   "status": "ACTIVE",
   "roomId": "LIB-101"
   }
4. curl DELETE http://localhost:8080/api/v1/sensors/TEMP-54
5. curl DELETE http://localhost:8080/api/v1/rooms/LIB-101