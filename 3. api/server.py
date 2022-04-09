from flask import Flask, jsonify, request
from analyser import analyse

app=Flask(__name__)


# GET
@app.route('/')
def default_method():
	print(__name__)
	return jsonify(data = "Complaint Analyser")


# POST
@app.route('/post', methods = ["POST"])
def post_method():
	complaint = request.form['data']
	output = analyse(complaint)
	print(output)
	return output

if __name__ == "__main__":
	app.run(host='0.0.0.0')


