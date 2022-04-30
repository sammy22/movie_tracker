
const express = require('express')
const axios = require('axios');
const session = require('express-session');
var bodyParser = require('body-parser')
var multer = require('multer');
const redis = require('redis');
const connectRedis = require('connect-redis');

var upload = multer();
const app = express()

app.use(bodyParser.urlencoded({ extended: true }));
app.use(upload.array());

//REdis for session management
//Configure redis client
const RedisStore = connectRedis(session)
const redisClient = redis.createClient({
    host: 'localhost',
    port: 6379,
    legacyMode: true
})
redisClient.on('error', function (err) {
    console.log('Could not establish a connection with redis. ' + err);
});
redisClient.on('connect', function (err) {
    console.log('Connected to redis successfully');
});
(async () => {
    await redisClient.connect();
})();


app.use(session({
    store: new RedisStore({ client: redisClient }),
    secret: 'secret$%^134',
    resave: false,
    saveUninitialized: false,
    cookie: {
        secure: false, // if true only transmit cookie over https
        httpOnly: false, // if true prevent client side JS from reading the cookie 
        maxAge: 1000 * 60 * 10 * 60 // session max age in miliseconds
    }
}))


app.use(express.static(__dirname + "/public"))
app.use('/css', express.static(__dirname + 'public/css'))
app.use('/js', express.static(__dirname + 'public/js'))
app.use('/imgs', express.static(__dirname + 'public/imgs'))
const port = 8000

app.set('views', './views')
app.set('view engine', 'ejs')


app.get('', (req, res) => {
    console.log('fetching main page')
    res.render('index', { message: 'Sign In to your account' })
})

app.get('/signup', (req, res) => {
    console.log('fetching signup page')
    return res.render('signup', { message: '' });
})


app.get('/search', (req, res) => {
    console.log('fetching search page')
    return res.render('search');
})


app.get('/search', (req, res) => {
    const sess = req.session;
    if (sess.email) {
        console.log('fetching search page')
        return res.render('search');
    } else {
        console.log('session timedout')
        res.render('index', { message: 'Sign In to your account' })
    }
})


app.post('/register', (req, res) => {

    var name = req.body.name;
    var email = req.body.email;
    var pass = req.body.password;

    var data = {
        "name": name,
        "email": email,
        "password": pass
    }

    axios.post('http://localhost:8080/signup', data)
        .then(response => {
            if (response.status == 200) {
                console.log('signup success')
                return res.redirect('/');
            }
        })
        .catch(error => {
            console.error('There was an error!', error);
            return res.render('signup', { message: 'User already exists, Try again' });
        });

})

app.post('/signin', (req, res) => {
    var email = req.body.email;
    var pass = req.body.password;
    const sess = req.session;


    var data = {
        "email": email,
        "password": pass
    }
    axios.post('http://localhost:8080/signin', data)
        .then(response => {
            console.log('sign in Success')
            if (response.status == 200) {
                const sess = req.session;
                const { email } = req.body
                sess.email = email
                return res.render('search');
            }
        })
        .catch(error => {
            console.error('There was an error!', error);
            return res.render('index', { message: 'User Doesnt exist or wrong creds' });
        });
})


app.post('/search', (req, res) => {

    var data = {
        "query": req.body.query,
        "type": req.body.type,
    }
    const sess = req.session;
    if (sess.email) {
        axios.post('http://localhost:8080/search', data)
            .then(response => {
                console.log('sending movie list')
                if (response.status == 200) {
                    res.render('movieList', { posts: response.data.searchresults });
                }
            })
            .catch(error => {
                console.log(error);
                return res.render('search');
            });
    } else {
        res.render('index', { message: 'Sign In to your account' })
    }
});

app.get("/logout", (req, res) => {
    req.session.destroy(err => {
        if (err) {
            return console.log(err);
        }
        res.redirect("/")
    });
});

app.get("/watchlist", (req, res) => {

    const sess = req.session;

    if (sess.email) {
        var data = {
            "email": sess.email
        }
        axios.post('http://localhost:8080/getwatchlist', data)
            .then(response => {
                if (response.status == 200) {
                    console.log('Sending Watchlist')
                    res.render('watchlist', { posts: response.data.watchlistresults });
                }
            })
            .catch(error => {
                console.log(error);
                return res.render('search');
            });
    } else {
        res.render('index', { message: 'Sign In to your account' })
    }
});


app.post('/details', (req, res) => {

    var data = {
        "mediaid": req.body.id
    }
    const sess = req.session;
    if (sess.email) {
        axios.post('http://localhost:8080/mediadetails', data)
            .then(response => {
                console.log('Sending details')
                if (response.status == 200) {
                    res.send(response.data);
                }
            })
            .catch(error => {
                console.log(error);
                return res.render('search');
            });
    } else {
        res.render('index', { message: 'Sign In to your account' })
    }

});


app.post('/addToWatchlist', (req, res) => {
    var data = {
        "mediaid": req.body.id,
        "email": req.session.email
    }
    const sess = req.session;
    if (sess.email) {
        axios.post('http://localhost:8080/addtowatchlist', data)
            .then(response => {
                if (response.status == 200) {
                    console.log('Added to watchlist success')
                    res.send(response.data);
                }
            })
            .catch(error => {
                res.statusCode = 500;
                res.send('failed')
                console.log(error);
            });
    } else {
        res.render('index', { message: 'Sign In to your account' })
    }

});


app.post('/getReviews', (req, res) => {
    var data = {
        "mediaid": req.body.id,
        "email": req.session.email
    }
    const sess = req.session;
    if (sess.email) {
        axios.post('http://localhost:8080/getreview', data)
            .then(response => {
                console.log('sending review')
                if (response.status == 200) {
                    res.send(response.data);
                }
            })
            .catch(error => {
                console.log(error);
                return res.render('search');
            });
    } else {
        res.render('index', { message: 'Sign In to your account' })
    }

});

app.post('/review', (req, res) => {
    var data = {
        "rating": parseFloat(req.body.rating),
        "email": req.session.email,
        "description": req.body.comments,
        "title": req.body.title,
        "mediaid": req.body.id
    }
    const sess = req.session;
    if (sess.email) {
        axios.post('http://localhost:8080/addreview', data)
            .then(response => {
                if (response.status == 200) {
                    res.send(response.data);
                }
            })
            .catch(error => {
                console.log(error);
                res.status(204).send();
            });
    } else {
        res.render('index', { message: 'Sign In to your account' })
    }

});



app.listen(port, () => console.info("listening on 8000"))