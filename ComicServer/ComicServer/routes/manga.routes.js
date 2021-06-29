const express = require('express');
const mysql = require('mysql');
const router = express.Router();

const connectionConfig = {
    host: "eu-cdbr-west-01.cleardb.com",
    user: "b8735e06265a7c",
    password: "5030e00f",
    database: "heroku_1582d4de0cfd5f2"
};

let connection;


handleDisconnect = () => {
    connection = mysql.createConnection(connectionConfig);

    connection.connect(function (err) {
        if (err) {
            console.log('error when connecting to db:', err);
            setTimeout(handleDisconnect, 2000);
        }
    });

    connection.on('error', function (err) {
        console.log('db error', err);
        if (err.code === 'PROTOCOL_CONNECTION_LOST') {
            handleDisconnect();
        } else {
            throw err;
        }
    });
}

handleDisconnect();

// /manga/comic GET ALL COMICS
router.get('/comic', (req, res, next) => {
    connection.query('SELECT * FROM manga', (error, result, fields) => {
        connection.on('err', (sqlError) => {
            console.log("[SQL ERROR]", sqlError);
        });

        if (result && result.length) {
            return res.send(JSON.stringify(result));
        }

        return res.json("NO DATA FROM SQL");
    });
});


// /manga/banner GET ALL BANNERS
router.get('/banner', (req, res, next) => {
    connection.query('SELECT * FROM banner', (error, result, fields) => {
        connection.on('error', (sqlError) => {
            console.log("[SQL ERROR]", sqlError);
        });

        if (result && result.length) {
            return res.send(JSON.stringify(result));
        }

        return res.json("NO DATA FROM SQL");
    });
});

// /manga/chapter/:id GET CHAPTER BY MANGA ID
router.get('/chapter/:manga_id', (req, res, next) => {
    connection.query('SELECT * FROM chapter where MangaID=?', [req.params.manga_id], (error, result, fields) => {
        connection.on('error', (sqlError) => {
            console.log("[SQL ERROR]", sqlError);
        });

        if (result && result.length) {
            return res.send(JSON.stringify(result));
        }

        return res.json("NO DATA FROM SQL");
    });
});


// /manga/chapter/:id GET IMAGES BY MANGA ID
router.get('/links/:chapter_id', (req, res, next) => {
    connection.query('SELECT * FROM link where ChapterId=?', [req.params.chapter_id], (error, result, fields) => {
        connection.on('error', (sqlError) => {
            console.log("[SQL ERROR]", sqlError);
        });
        if (result && result.length) {
            return res.send(JSON.stringify(result));
        }

        return res.json("NO DATA FROM SQL");
    });
});


//Getting all Category
router.get("/categories", (req, res, next) => {
    connection.query('SELECT * FROM category', function (error, result, fields) {
        connection.on('error', function (err) {
            console.log('[MYSQL ERROR]', err);
        });
        if (result && result.length) {

            res.end(JSON.stringify(result));

        } else {

            res.end(JSON.stringify("No Category Available"));
        }

    })

});

//Getting all Filter
router.post("/filter", (req, res, next) => {

    var post_data = req.body; //GET POST DATA from POST Request
    var array = JSON.parse(post_data.data); //PARSE 'data' FROM POST REQUEST TO JSON ARRAY
    var query = "SELECT * FROM manga WHERE ID IN (SELECT MangaID FROM mangacategory "; //default query
    if (array.length > 0) {
        query += "GROUP BY MangaID ";
        if (array.length == 1) { //if user just submit 1 category
            query += "HAVING SUM(CASE WHEN CategoryID =" + array[0] + " THEN 1 ELSE 0 END) > 0)";

        } else { //user submits more thean one category
            for (var i = 0; i < array.length; i++) {
                if (i == 0) //frst condition
                    query += "HAVING SUM((CASE WHEN CategoryID =" + array[0] + " THEN 1 ELSE 0 END) > 0 OR ";
                else if (i == array.length - 1) //last condition
                    query += " (CASE WHEN CategoryID =" + array[i] + " THEN 1 ELSE 0 END )>0)) ";
                else
                    query += " (CASE WHEN CategoryID =" + array[i] + " THEN 1 ELSE 0 END )>0 OR ";
            }
        }
        
        connection.query(query, function (error, result, fields) {
            connection.on('error', function (err) {
                console.log('[MYSQL ERROR]', err);
            });
            if (result && result.length) {

                res.end(JSON.stringify(result));

            } else {
                res.end(JSON.stringify("No comic Available here"));
            }

        })

    }

});
//Search Manga by Name.
router.post("/search", (req, res, next) => {

    var post_data = req.body; //get body post
    var name_search = post_data.search; //GET 'search' from the POST request

    var query = "SELECT *FROM manga WHERE Name LIKE'%" + name_search + "%'";
    connection.query(query, function (error, result, fields) {
        connection.on('error', function (err) {
            console.log('[MYSQL ERROR]', err);
        });
        if (result && result.length) {

            res.end(JSON.stringify(result));

        } else {

            res.end(JSON.stringify("No search result here"));
        }

    })


});

module.exports = router;