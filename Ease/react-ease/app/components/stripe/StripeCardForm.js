var React = require('react');

class StripeCardForm extends React.Component {
  constructor(props){
    super(props);

    this.onSubmit = this.onSubmit.bind(this);
    this.setOutcome = this.setOutcome.bind(this);
    this.stripe = Stripe('pk_test_95DsYIUHWlEgZa5YWglIJHXd');
    this.elements = this.stripe.elements();
    this.card = this.elements.create('card', {
      style: {
        base: {
          iconColor: '#666EE8',
          color: '#31325F',
          lineHeight: '40px',
          fontWeight: 300,
          fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
          fontSize: '15px',

          '::placeholder': {
            color: '#CFD7E0'
          },
        },
      }
    });
  }
  setOutcome(result) {
    if (result.token) {
      console.log('stripe token :', result.token.id);
      this.props.tokenCallback(result.token.id);
    } else if (result.error) {
      console.log('stripe error :', result.error.message);
    }
  }
  onSubmit(e){
    e.preventDefault();
    var extraDetails = {
      name: document.querySelector('input[name=cardholder-name]').value
    };
    this.stripe.createToken(this.card, extraDetails).then(this.setOutcome);
  }
  componentDidMount(){
    this.card.mount('#card-element');
    this.card.on('change', function(event) {
      this.setOutcome(event);
    }.bind(this));
  }
  render(){
    return (
        <form onSubmit={this.onSubmit}>
          <div class="group display-flex flex_direction_column">
            <label htmlFor="cardholder-name">Card holder</label>
            <input name="cardholder-name" id="cardholder-name" class="input_unstyle modal_input" placeholder="Jane Doe"/>
          </div>
          <div class="group">
            <label>Card</label>
            <div id="card-element" class="field"></div>
          </div>
          <button class="button-unstyle big-button" type="submit">
            Update the bill !
          </button>
        </form>
    )
  }
}

module.exports = StripeCardForm;