
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
(async() => {
    console.log('before start');
  
    await redisClient.connect();
    
    console.log('after start');
  })();


app.use(session({
    store: new RedisStore({ client: redisClient }),
    secret: 'secret$%^134',
    resave: false,
    saveUninitialized: false,
    cookie: {
        secure: false, // if true only transmit cookie over https
        httpOnly: false, // if true prevent client side JS from reading the cookie 
        maxAge: 1000 * 60 * 10 // session max age in miliseconds
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
    res.render('index', { message: 'Sign In to your account' })
})

app.get('/signup', (req, res) => {
    return res.render('signup', { message: '' });
})


app.get('/search', (req, res) => {
    return res.render('search');
})


app.get('/search', (req, res) => {
    const sess = req.session;
    if (sess.email) {
        return res.render('search');
    } else {
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
                return res.redirect('/');
            }
        })
        .catch(error => {
            // console.error('There was an error!', error);
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
                console.log(response.status)
                if (response.status == 200) {
                    const sess = req.session;
                    const { email } = req.body
                    sess.email = email
                    return res.render('search');
                }
            })
            .catch(error => {
                return res.render('index', { message: 'User Doesnt exist or wrong creds' });
            });
})


app.post('/search', (req, res) => {

    var data = {
        "query": req.body.query,
        "type": req.body.type,
    }
    const sess = req.session;
    console.log(sess)
    if (sess.email) {
    axios.post('http://localhost:8080/search', data)
        .then(response => {
            console.log(response.status)
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


app.post('/details', (req, res) => {
    // console.log(req.body)
    
    var data = {
        "mediaid": req.body.id
    }
    const sess = req.session;
    if (sess.email) {
    axios.post('http://localhost:8080/mediadetails', data)
        .then(response => {
            console.log(response.status)
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

app.listen(port, () => console.info("listening on 8000"))