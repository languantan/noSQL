# Load the Rails application.
require File.expand_path('../application', __FILE__)

# Initialize the Rails application.
Braintree::Application.initialize!
Braintree::Configuration.environment = :sandbox
Braintree::Configuration.merchant_id = 'qt3jhmqc8swzwq96'
Braintree::Configuration.public_key = 'zv8rydyzqh6gxt69'
Braintree::Configuration.private_key = '82fe1bd1e20edb5608e6e9dad9df7a86'