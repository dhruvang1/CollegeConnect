//creating the server
var express = require('express');
var app = express();
var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
//root route
app.get('/', function(req, res){
    res.send('Hello World');
});


//ip = 139.59.61.95
app.listen(1337);

//mongo

// include the mongodb module
var mongo = require('mongodb');

// create a server instance
var serverInstance = new mongo.Server('localhost', 27017);

// retrieve a database reference
var dbref = new mongo.Db('CConnect', serverInstance);

// connect to database server
dbref.open(function(err, dbref) {
    // now a connection is established
    if(err)
    	console.log("ERR");
    else
    	console.log("CONNECTED_TO_DB");
		// close a database connection
		dbref.close();
});

app.post('/register', function(req,res) {
	//validate login
	var json_data = req.body;

	dbref.open(function(err, dbref) {
	    // now a connection is established
	    if(!err){
	    	// console.log("CONNECTED_TO_DB");
	    	var Login = dbref.collection('Login');
		    Login.find({"username": req.body.username}).toArray(function (err, result) {
	        	if (err) {
	        		console.log('Error__: ',JSON.stringify(err));
	        	} else if (result.length) {
	        		res.send({"status":"username already exists"});
	        	} else {
	        		//username available for register
	        		//insert in Login
	        		var new_login = {"username":json_data.username, "password":json_data.password};
	        		Login.insert(new_login, function (err, result) {
	    	        	if (err) {
	    	        	  console.log(err);
	    	        	} else {
	    	        	  console.log('Inserted into the "Login" collection successfully.');
	    	        	}
    	        	});
	        		//insert in Profiles
	        		var Profiles = dbref.collection("Profiles");
	        		var new_user = {
	        			"username": json_data.username,
	        			"name": json_data.name,
	        			"entry_no": json_data.entry_no,
	        			"degree": json_data.degree,
	        			"year": json_data.year,
	        			"hostel": json_data.hostel,
	        			"contact": json_data.contact,
	        			"bio": json_data.bio,
	        			"photo": json_data.photo,
	        			"events": [],
	        			"block_list": [],
	        			"mute_list": []
	        		};
	        		Profiles.insert(new_user, function (err, result) {
	    	        	if (err) {
	    	        	  console.log(err);
	    	        	} else {
	    	        	  console.log('Inserted into the "Profiles" collection successfully.');
	        			  res.send({"status":"success"});
	    				  dbref.close();
	    	        	}
    	        	});
	        	}
	        });
	    }
	});
});

app.post('/login', function(req,res) {
	//validate login
	var json_data = req.body;

	dbref.open(function(err, dbref) {
	    // now a connection is established
	    if(!err){
	    	// console.log("CONNECTED_TO_DB");
	    	var Login = dbref.collection('Login');
		    Login.find({"username": req.body.username}).toArray(function (err, result) {
	        	if (err) {
	        		console.log('Error__: ',JSON.stringify(err));
	        	} else if (result.length) {
	        		if(result[0].password == req.body.password) {
	        			var Profiles = dbref.collection('Profiles');
	        			Profiles.find({"username": req.body.username}).toArray(function (err, result2) {
	        				if(err) {
	        					console.log('Error__: ',JSON.stringify(err));
	        				} else if (result2.length) {
	        					res.send({"status":"success", "name":result2[0].name});
	        					dbref.close();
	        				}
	        				else {
	        					console.log("Profile doesn't exist");
	        					res.send({"status":"no such profile"});
	        					dbref.close();
	        				}

	        			});
	        		}
	        		else {
	        			res.send({"status":"invalid password"});
	        			dbref.close();
	        		}
	        	} else {
	        		// console.log('Error: No data found');
	        		res.send({"status":"username not found"});
        			dbref.close();
	        	}
	        });
	    }
	});
});

app.get('/profile/:username', function(req,res) {
	//validate login
	dbref.open(function(err, dbref) {
	    // now a connection is established
	    if(!err){
	    	// console.log("CONNECTED_TO_DB");
	    	var Profiles = dbref.collection('Profiles');
		    Profiles.find({"username": req.params.username}).toArray(function (err, result) {
	        	if (err) {
	        		console.log('Error__: ',JSON.stringify(err));
	        		dbref.close();
	        	} else if (result.length) {
	        		var user_data = {
	        			"username": result[0].username,
	        			"name": result[0].name,
	        			"entry_no": result[0].entry_no,
	        			"degree": result[0].degree,
	        			"year": result[0].year,
	        			"hostel": result[0].hostel,
	        			"contact": result[0].contact,
	        			"bio": result[0].bio,
	        			"photo": result[0].photo
	        		};
	        		// console.log(user_data);
	        		res.send(user_data);
	        		dbref.close();
	        	} else {
	        		console.log('Error: No data found');
        			dbref.close();
	        	}
	        });
	    }
	});
});

app.get('/feed/:index', function(req,res) {
	//validate login
	var index = req.params.index;
	var start_i=0;
	var end_i=0;
	dbref.open(function(err, dbref) {
	    // now a connection is established
	    if(!err){
	    	// console.log("CONNECTED_TO_DB");
	    	var Events = dbref.collection('Events');
		    Events.find().toArray(function (err, result) {
	        	if (err) {
	        		console.log('Error__: ',JSON.stringify(err));
	        		dbref.close();
	        	} else {
	        		if(parseInt(index)==0)
	        			end_i = result.length + 1;
	        		else
	        			end_i = parseInt(index);
	        		start_i = end_i - 10;
				    Events.find({event_id:{$gt:start_i, $lt:end_i}}).toArray(function (err, result) {
				    	var new_feed = [];
			        	if (err) {
			        		console.log('Error__: ',JSON.stringify(err));
			        	} else if (result.length) {
			        		var i;
			        		for(i=0; i<result.length; i++) {
			        			var feed_element = {
			        				event_id: result[i].event_id,
			        				title: result[i].title,
			        				photo: result[i].photo,
			        				uploader: result[i].uploader,
			        				date: result[i].date, 
			        				time: result[i].time,
			        				venue: result[i].venue,
			        				desc: result[i].desc
			        			};
			        			new_feed.push(feed_element);
			        		}
			        		res.send({"feed":new_feed});
			        	} else {
			        		console.log('Error: No more data found');
			        		res.send({"feed":[]});
			        	}
		        		dbref.close();
			        });
	        	}
	        });	
	    }
	});
});

app.post('/event', function(req,res) {
	//validate login
	var json_data = req.body;

	dbref.open(function(err, dbref) {
	    // now a connection is established
	    if(!err){
	    	// console.log("CONNECTED_TO_DB");
	    	var Events = dbref.collection('Events');
		    Events.find().toArray(function (err, result) {
	        	if (err) {
	        		console.log('Error__: ',JSON.stringify(err));
	        		dbref.close();
	        	} else {
	        		var new_event_id = result.length+1;
			    	var new_event = {
			    		event_id: new_event_id,
			    		photo: json_data.photo,
			    		title: json_data.title,
			    		uploader: json_data.uploader,
			    		date: json_data.date, 
			    		time: json_data.time,
			    		venue: json_data.venue,
			    		desc: json_data.desc
			    	}
		    		Events.insert(new_event, function (err, result) {
			        	if (err) {
			        	  console.log(err);
			        	} else {
			        	  console.log('Inserted into the "Events" collection successfully.');
		    			  res.send({"status":"success"});
        				  dbref.close();
			        	}
		        	});
	        	}
	        });
	    }
	});
});

app.put('/update/:username', function(req,res) {
	var json_data = req.body;
	console.log(json_data);
	dbref.open(function(err, dbref) {
	    // now a connection is established
	    if(!err){
	    	var Profiles = dbref.collection("Profiles");
		    Profiles.find({"username": req.params.username}).toArray(function (err, result) {
	        	if (err) {
	        		console.log('Error__: ',JSON.stringify(err));
	        		res.send({"status":"fail:mongo_error"});
	        	} else if (result.length) {
	        		usr = result[0];
	        		var new_usr = {
	        			"username": result[0].username,
	        			"name": result[0].name,
	        			"entry_no": result[0].entry_no,
	        			"degree": result[0].degree,
	        			"year": result[0].year,
	        			"hostel": result[0].hostel,
	        			"contact": result[0].contact,
	        			"bio": result[0].bio
	        		}
		    	    dbref.collection('Profiles').updateOne(
		    	          { "username" : req.params.username },
		    	          { $set: {
		    	          		"name": json_data.name,
		    	          		"entry_no": json_data.entry_no,
		    	          		"degree": json_data.degree,
		    	          		"year": json_data.year,
		    	          		"hostel": json_data.hostel,
		    	          		"contact": json_data.contact,
		    	          		"bio": json_data.bio
		    	            } },
		    	          function(err, results) {
		    	          	if(!err){
		    	          		if(req.body.password != "") {
			    	          		var Login = dbref.collection("Login");
			    	          		Login.find({"username":req.params.username}).toArray(function (err, result) {
			    	          			if(err){
			    	          				console.log('Error__: ',JSON.stringify(err));
			    	          				res.send({"status":"fail:mongo_error"});
			    	          			}
			    	          			else if (result.length) {
			    	          				dbref.collection('Login').updateOne(
			    	          					{"username":req.params.username},
			    	          					{ $set: {"password": req.body.password}},
			    	          					function(err, result) {
			    	          						if(!err)
			    	          							res.send({"status":"success"});
			    	          						else
			    	          							res.send({"status":"fail"});
			    	    							dbref.close();
			    	          					});
			    	          			}
			    	          			else {
			    	          				console.log('Error: No data found');
		        							res.send({"status":"fail: No data found"});
			    	          			}
			    	          		});
		    	          		}
		    	          		else{
		    	          			res.send({"status":"success"});
		    	          			dbref.close();
		    	          		}
		    	          	}
		    	          	else{
		    	          		res.send({"status":"fail"});
		    	          		dbref.close();
		    	          	}
		    	       });
	        	} else {
	        		console.log('Error: No data found');
	        		res.send({"status":"fail: No data found"});
	        	}
	        });
	    }
	});
});

app.post('/search/p', function(req,res) {
	//validate login
	var json_data = req.body;
	var reg_name = new RegExp(".*"+json_data.name+".*", "i");
	var reg_entry_no = new RegExp(".*"+json_data.entry_no+".*", "i");
	var reg_hostel = new RegExp(".*"+json_data.hostel+".*", "i");
	var reg_degree = new RegExp(".*"+json_data.degree+".*", "i");
	//name entry_no hostel department
	//username name entry_no
	dbref.open(function(err, dbref) {
	    // now a connection is established
	    if(!err){
	    	// console.log("CONNECTED_TO_DB");
	    	var Profiles = dbref.collection('Profiles');
		    Profiles.find().toArray(function (err, result) {
	        	if (err) {
	        		console.log('Error__: ',JSON.stringify(err));
	        		dbref.close();
	        	} else {
				    Profiles.find({"name": reg_name, "entry_no": reg_entry_no, "hostel": reg_hostel, "degree": reg_degree}).toArray(function (err, result) {
				    	var search_result = [];
			        	if (err) {
			        		console.log('Error__: ',JSON.stringify(err));
			        	} else if (result.length) {
			        		var i;
			        		for(i=0; i<result.length; i++) {
			        			var search_element = {
			        				username: result[i].username,
			        				name: result[i].name,
			        				entry_no: result[i].entry_no,
			        			};
			        			search_result.push(search_element);
			        		}
			        		res.send({"profile_search":search_result});
			        	} else {
			        		console.log('Profile Search: No results');
			        		res.send({"profile_search":[]});
			        	}
		        		dbref.close();
			        });
	        	}
	        });	
	    }
	});
});