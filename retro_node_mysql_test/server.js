const express = require('express');
const bodyparser = require('body-parser');
const app = express();
const mysql = require('mysql');


/* student test 용///////////
const con = mysql.createConnection (
    {
        host : 'localhost',
        user : 'root',
        password : 'rud015146',
        database : 'test',
    }
);
////////// student test용///////////////
*/

const con = mysql.createConnection (
    {
        host : 'localhost',
        user : 'root',
        password : 'rud015146',
        database : 'project',
    }
);

con.connect (function(err)
{
    if(err) throw err;
    console.log("Connected !...");
});


app.use(bodyparser.json());
app.use(bodyparser.urlencoded({
    extended : true
}));


/*    //////////////////////////////// student test용 //////////////////////////////
////////// GET //////////
app.get('/get', function(req, res)  {
    var name = req.query.name;
    //let sql = 'select course from student where name = "' + name + '"';
    var sql = 'select * from student where name = "' + name + '"';

    con.query(sql, (err, result) =>{
        
        if(err){
            console.log(err);
            res.status(500).send("Internal Server Error");
        } else {
            if(result.length === 0) {
                console.log("GET 통신, name: " + name + "이 DB에 존재하지 않음");
                res.status(500).send("No Name");
            } else {
                //res.send(JSON.stringify(result[0].course))
                res.send(JSON.stringify(result[0]))
                console.log("GET 통신,  name: " + name + " 불러오기 성공");
            }
        }

    });
    
});
////////// GET //////////



////////// POST //////////
app.post('/post', function(req, res) {
    var name = req.body.name;
    var course = req.body.course;

    var sql = 'insert into student(name, course) values ("' + name + '", "' + course +'")';

    con.query(sql, function(err, result) {
        if(err) {
            console.log("POST 동작 오류");
            res.status(500).send("Internal Server Error");
        } else {
            res.json({'status':'success', id:result.insertId})

            console.log("POST 통신, name: " + name + ", course : " + course + " 입력 성공");
        }
        
    });

    
});
////////// POST //////////





////////// PUT //////////
app.put('/put', function(req, res) {
    var name = req.body.name;
    var course = req.body.course;

    var sql = 'select * from student where name = "' + name + '"';

    con.query(sql, (err, result) => {
        if(err) {
            console.log(err);
            res.status(500).send("Internal Server Error");
        } else {
            if(result.length === 0) {
                console.log("PUT 통신, name: " + name + " 이 DB에 존재하지 않음");
                res.status(500).send("No Name");
            } else {
                var sql = 'update student set course = "' + course + '" where name = "' + name + '"';

                con.query(sql, function(err, result) {
                    if(err) {
                        console.log("PUT 동작 오류");
                        res.status(500).send("Internal Server Error");
                    } else {
                        res.send(result)
            
                        console.log("PUT 통신,  name: " + name + " 학생의 과목을 " + course + "로 변경 성공");
                    }
                })                
            }
        }
    });    
})
////////// PUT //////////





////////// DELETE //////////
app.delete('/delete', function(req, res) {
    var name = req.query.name;

    var sql = 'select * from student where name = "' + name + '"';

    con.query(sql, function(err, result) {
        if(err) {
            console.log(err)
            res.status(500).send("Internal Server Error");
        } else {
            if(result.length === 0) {
                console.log("DELETE 통신, name: " + name + " 이 DB에 존재하지 않음");
                res.status(500).send("No Name");
            } else {
                var sql = 'delete from student where name = "' + name + '"';

                con.query(sql, function(err, result) {
                    if(err) {
                        console.log("DELETE 동작 오류");
                        res.status(500).send("Internal Server Error");
                    } else {
                        res.send(result)

                        console.log("DELETE 통신,  name: " + name + " 학생 삭제 성공");
                    }
                })
            }

        }
        
    });
    
})
////////// DELETE //////////
/////////////////////////////////////student test용 /////////////////////////////
*/



////////// GET Data//////////
app.get('/get-data', function(req, res)  {
    var send_name = req.query.send_name;
    var rec_name = req.query.rec_name;
    
    var sql = 'select * from user where send_name = "' + send_name + '" and rec_name = "' + rec_name + '" and status = "0"';

    con.query(sql, (err, result) =>{
        
        if(err) {
            console.log(err);
            res.status(500).send("Internal Server Error");
        } else {
            if(result.length === 0) {
                console.log("GET Data 통신, 입력 정보: " + send_name + ", " + rec_name + " 이 DB에 존재하지 않음");
                res.status(500).send("No Name");
            } else {
                res.send(JSON.stringify(result[0]))
                console.log("GET Data 통신, send_name: " + send_name + ", rec_name: " + rec_name + "에 대한 정보 불러오기 성공");
            }
        }

    });
    
});
////////// GET Data//////////


////////// GET Coordinate//////////
app.get('/get-coordinate', function(req, res) {
    var send_name = req.query.send_name;
    var rec_name = req.query.rec_name;

    var sql = 'select * from building_loc where name = (select rec_loc from user where send_name = "' + send_name + '" and rec_name = "' + rec_name + '" and status = "0")';

    con.query(sql, function(err, result) {
        
        if(err) {
            console.log(err);
            res.status(500).send("Internal Server Error");
        } else {
            if(result.length === 0) {
                console.log("GET Coordinate 통신, 해당 건물이 DB에 존재하지 않음");
                res.status(500).send("No Name");
            } else {
                res.send(JSON.stringify(result[0]))
                console.log("GET Coordinate 통신, Coordinate 불러오기 성공");
            }
        }

    });
    
});
////////// GET Coordinate//////////


////////// PUT Robot//////////
app.put('/put-robot-coordinate', function(req, res) {
    var robot_idx = req.body.robot_idx;
    var latitude = req.body.latitude;
    var longitude = req.body.longitude;

    var sql = 'select * from robot where robot_idx = "' + robot_idx + '"';

    con.query(sql, function(err,result) {
        if(err) {
            console.log(err);
            res.status(500).send("Internal Server Error");
        } else {
            if(result.length === 0) {
                console.log("PUT Robot 통신, robot_idx: " + robot_idx + "이 DB에 존재하지 않음");
                res.status(500).send("No robot_idx");
            } else {
                var sql = 'update robot set latitude = "' + latitude + '", longitude = "' + longitude + '" where robot_idx = "1"';

                con.query(sql, function(err, result) {
                    if(err) {
                        console.log("PUT Robot 동작 오류");
                        res.status(500).send("Internal Server Error");
                    } else {           
                            res.send(result)
                        
                            console.log("PUT Robot 통신, robot_idx: 1의 latitude: " + latitude + ", longitude: " + longitude + " 로 변경 성공");
                    }
            
                })
            }
        }
    })

})
////////// PUT Robot//////////



////////// PUT User Status//////////
app.put('/put-user-status', function(req, res) {
    var send_name = req.body.send_name;
    var rec_name = req.body.rec_name;
    var status = req.body.status;

    var sql = 'select * from user where send_name = "' + send_name + '" and rec_name = "' + rec_name + '" and status = "0"';

    con.query(sql, function(err,result) {
        if(err) {
            console.log(err);
            res.status(500).send("Internal Server Error");
        } else {
            if(result.length === 0) {
                console.log("PUT User Status 통신, 정보가 DB에 존재하지 않음"+ send_name + ", " + rec_name + ", " + status);
                res.status(500).send("No User Info in DB");
            } else {
                var sql = 'update user set status = "' + status + '" where send_name = "' + send_name + '" and rec_name = "' + rec_name + '"';

                con.query(sql, function(err, result) {
                    if(err) {
                        console.log("PUT User Status 동작 오류");
                        res.status(500).send("Internal Server Error");
                    } else {           
                            res.send(result)
                        
                             console.log("PUT User Status 통신, Status 변경 성공");
                    }
            
                })
            }
        }
    })

})
////////// PUT User Status//////////


////////// POST User //////////
app.post('/post-user', function(req, res) {
    var st_id = req.body.st_id;
    var send_name = req.body.send_name;
    var rec_name = req.body.rec_name;
    var type = req.body.type;
    var send_loc = req.body.send_loc;
    var rec_loc = req.body.rec_loc;
    var time_info = req.body.time_info;

    var sql = 'insert into user(st_id, send_name, rec_name, type, send_loc, rec_loc, time_info, status) values ("' + st_id + '", "' + send_name + '", "' + rec_name + '", "' + type + '", "' + send_loc + '", "' + rec_loc + '", "' + time_info + '", "0")';

    con.query(sql, function(err, result) {
        if(err) {
            console.log("POST User 동작 오류");
            res.status(500).send("올바르지 않은 입력값");
        } else {
            res.json({'status':'success', id:result.insertId})

            console.log("POST User 통신, DB에 user 정보 입력 성공");
        }
        
    });

    
});
////////// POST User //////////


////////// POST Reservation Data //////////
app.post('/post-reservation-data', function(req, res) {
    var st_id = req.body.st_id;
    var loc = req.body.loc;
    var type = req.body.type;
    var time_info = req.body.time_info;
    var load_type = req.body.load_type;    
    

    var sql = 'insert into ' + loc + ' (st_id, type, time_info, load_type) values ("' + st_id + '", "' + type + '", "' + time_info + '", "' + load_type +  '")';

    con.query(sql, function(err, result) {
        if(err) {
            console.log("POST Reservatioin Data 동작 오류");
            console.log(err)
            res.status(500).send("올바르지 않은 입력값");
        } else {
            res.json({'status':'success', id:result.insertId})

            console.log("POST Reservation Data 통신, DB에 건물 예약 정보 입력 성공");
        }
        
    });

    
});
////////// POST Send Data //////////




////////// DELETE User Data //////////
app.delete('/delete-user', function(req, res) {
    var send_name = req.query.send_name;
    var rec_name = req.query.rec_name

    var sql = 'select * from user where send_name = "' + send_name + '" and rec_name = "' + rec_name + '" and status = "0"';

    con.query(sql, function(err, result) {
        if(err) {
            console.log(err)
            res.status(500).send("Internal Server Error");
        } else {
            if(result.length === 0) {
                console.log("DELETE User 통신, send_name: " + send_name + ", rec_name: " + rec_name + " 이 DB에 존재하지 않음");
                res.status(500).send("No Name");
            } else {
                var st_id = result[0].st_id;
                var time_info = result[0].time_info;
                var send_loc = result[0].send_loc;
                var rec_loc = result[0].rec_loc;

                switch(send_loc) {
                    case "기숙사":
                        send_loc = "dormitory";
                        break;
                    case "농심관":
                        send_loc = "nongsim";
                        break;
                    case "학생회관":
                        send_loc = "student_union";
                        break;
                    case "도서관":
                        send_loc = "library";
                        break;
                }

                switch(rec_loc) {
                    case "기숙사":
                        rec_loc = "dormitory";
                        break;
                    case "농심관":
                        rec_loc = "nongsim";
                        break;
                    case "학생회관":
                        rec_loc = "student_union";
                        break;
                    case "도서관":
                        rec_loc = "library";
                        break;
                }

                var sql = 'delete from user where send_name = "' + send_name + '" and rec_name = "' + rec_name + '" and status = "0"';

                con.query(sql, function(err, result) {
                    if(err) {
                        console.log("DELETE User 동작 오류");
                        res.status(500).send("Internal Server Error");
                    } else {
                        var sql = 'delete from ' + send_loc + ' where st_id = "' + st_id + '" and time_info = "' + time_info + '" and status = "0"';

                        con.query(sql, function(err, result) {
                        if(err) {
                            console.log("DELETE send_loc 동작 오류");
                            console.log(err)
                        } else {
                            
                            }
                        })

                        var sql = 'delete from ' + rec_loc + ' where st_id = "' + st_id + '" and time_info = "' + time_info + '" and status = "0"';
                        
                        con.query(sql, function(err, result) {
                            if(err) {
                                console.log("DELETE rec_loc 동작 오류");
                                console.log(err)
                            } else {
                                
                            }
                        })
                        
                        res.send(result)
                        console.log("DELETE User 통신, send_name: " + send_name + ", rec_name: " + rec_name + " 예약 삭제 성공");
                        
                    }
                })
                

            }
        }
    });
    
})
////////// DELETE User Data //////////





app.listen(3000, () => {
    console.log('Server started on port 3000...');
})