# Example App Backend

This is a really simple Java webapp that you can use to test Paymentez [example iOS apps](https://github.com/paymentez/paymentez-ios-example) and
[example Android apps](https://github.com/paymentez/paymentez-android-example).

This is intended for example purposes only: you'll likely need something more serious for your production apps.

## Deploying to Heroku

To deploy this for free on Heroku, click this button:

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

Then set the `backendBaseURL` variable in our example apps to your Heroku URL (it'll be in the format https://my-example-app.herokuapp.com).

Happy testing!



## Running Locally

Make sure you have Java and Maven installed.  Also, install the [Heroku CLI](https://cli.heroku.com/).

```sh
$ git clone https://github.com/paymentez/example-java-backend.git
$ cd example-java-backend
$ mvn install
$ heroku local:start
```

Your app should now be running on [localhost:5000](http://localhost:5000/).