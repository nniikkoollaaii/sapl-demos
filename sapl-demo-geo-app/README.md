# Submodule SAPL Demo Geo Android App

This Android app can be used as an interface to connect to the [SAPL Demo Geo Server](https://github.com/heutelbeck/sapl-demos/tree/master/sapl-demo-geo-server). The demo scenario sets place in the aviation industry, where a flight attendant wants to access passenger related information (the so called *Passenger Information List*, PIL) on her personal mobile device. Depending on the geographic location she/he currently is, the access is restricted to only a few information. For example, being in her/his apartment at home she/he can only see some very basic data, whereas she/he can access all personal information of the passengers while being in the aircraft.

A video explaining the scenario and app behavior can be found [here](https://www.youtube.com/watch?v=OtSotMNmX9Y).

## Installation

The URL to the authorization server can be set in `PilRequestSender.java`.
For SSL-authentication and encryption with the server the app requires two certificates to be installed:
* A client certificate that must be known to the server (e.g. signed by the same CA)
* A certificate to identify the server.