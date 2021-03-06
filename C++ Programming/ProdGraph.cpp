//reads data and displays a bar graph showing productivity for each plant
#include <iostream>
#include <cmath>
using namespace std;
const int NUM_OF_PLANTS = 4;

void inputData(int a[], int lastPlantNumber);
//Precondition: lastPlantNumber is the declared size of array a
//Postcondition: for plantNumber = 1 through lastPlantNumber:
//a[plantNumber-1] equals the toatal production for plant number plantNumber

void scale(int a[], int size);
//Precondition: a[0] through a[size-1] each has a nonnegative value.
//Postcondition: a[i] has been changed to the number of 1000s (round to
//an integer) that were originally in a[i], for all i such that 0 <= i <= size-1

void graph(const int asteriskCount[], int lastPlantNumber);
//Precondition: a[0] through a[lastPlantNumber-1] have nonnegative values.
//Postcondition: A bar graph has been displayed saying that plant
//number N has produced a[N-1] 1000s of units, for each N such that
//1 <= N <= lastPlantNumber

void getTotal(int& sum);
//reads nonnegative integers from the keyboard and
//places their total in sum.

void printAsterisks(int n);
//Prints n asterisks to the screen

int main()
{
	int production[NUM_OF_PLANTS];

	cout << "This program displays a graph showing\n"
		 << "production for each plant in the company.\n";

	inputData(production, NUM_OF_PLANTS);
	scale(production, NUM_OF_PLANTS);
	graph(production, NUM_OF_PLANTS);
	return 0;
}

void inputData(int a[], int lastPlantNumber)
{
	for (int plantNumber = 1; plantNumber <= lastPlantNumber; plantNumber++)
	{
		cout << endl
			 << "Enter production data for plant number "
			 << plantNumber << endl;
		getTotal(a[plantNumber-1]);
	}
}

void scale(int a[], int size)
{
	for (int index = 0; index < size; index++)
		a[index] = round(a[index]/1000.0);
}

void graph(const int asteriskCount[], int lastPlantNumber)
{
	cout << "\nUnits produced in thousands of units:\n";
	for (int plantNumber = 1; plantNumber <= lastPlantNumber; plantNumber++)
	{
		cout << "Plant #" << plantNumber << " ";
		printAsterisks(asteriskCount[plantNumber-1]);
		cout << endl;
	}
}

void getTotal(int& sum)
{
	cout << "Enter number of units produced by each department.\n"
		 << "Append a negative number to the end of the list.\n";

	sum = 0;
	int next;
	cin >> next;
	while (next >= 0)
	{
		sum += next;
		cin >> next;
	}

	cout << "Total = " << sum << endl;
}


void printAsterisks(int n)
{
	for (int i = 1; i <= n; i++)
	{
		cout << "*";
	}
}