import pickle 
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer

# Pickle files
model_filename = "temp_pickle_nbc.sav"
vectorizer_filename = "temp_vectorizer.pickle"

dictionary = {0: "Savings Account", 1: "Credit Card", 
2: "Credit Reporting", 3: "Mortgage", 4: "Student Loan"}

def analyse(complaint):

	complaint = complaint.lower()
	
	# Input Cleaning
	stop_words = set(stopwords.words('english'))
	
	lemmatizer = WordNetLemmatizer()

	# New Input String After Lemmatization
	final_string = " ".join([lemmatizer.lemmatize(word) for word in complaint.split() if word not in stop_words])
	print(final_string)
	# Transform string before prediction
	vectorizer = pickle.load(open(vectorizer_filename, 'rb')) # load vectorizer pickle
	W = vectorizer.transform([final_string]) # final vector

	# Load Pickle file
	model = pickle.load((open(model_filename, "rb")))

	# Predict
	output = model.predict(W.toarray())[0]

	return dictionary[output]

# data = pd.read_csv("bank_dataset.csv")

# # input string
# row1 = data.iloc[3]

# string = row1['Complaint']

# print(analyse(string))

string = "My credit reports are inconsistently as compared to my actually usages. there is surely some error in your software. please fix this. my account number is xxxxxxxx789"
print(analyse(string))