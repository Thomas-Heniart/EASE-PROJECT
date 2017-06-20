var React = require('react');
var classnames = require('classnames');

class TeamMultiAppUserSelect extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      dropdown: false
    };
    this.onMouseDown = this.onMouseDown.bind(this);
    this.onMouseUp = this.onMouseUp.bind(this);
    this.pageClick = this.pageClick.bind(this);
    this.setDropdown = this.setDropdown.bind(this);
  }
  setDropdown(state){
    this.setState({dropdown: state});
  }
  onMouseDown(){
    this.mouseInDropDown = true;
  }
  onMouseUp(){
    this.mouseInDropDown = false;
  }
  pageClick(e){
    if (this.mouseInDropDown)
      return;
    this.setState({dropdown: false});
  }
  componentDidMount(){
    window.addEventListener('mousedown', this.pageClick, false);
  }
  componentWillUnmount(){
    window.removeEventListener('mousedown', this.pageClick, false);
  }
  render() {
    const webInfo = this.props.website_information;
    const receivers = this.props.receivers;
    const myId = this.props.myId;
    const selectedReceivers = this.props.selectedReceivers.map(function (receiver) {
      return (
          <div class="receiver_wrapper" key={receiver.id}>
            <div class="receiver">
              <span class="receiver_name">{receiver.username}{receiver.id === myId ? '(you)': null}</span>
              <i class="fa fa-unlock-alt mrgnLeft5"/>
              <button class="button-unstyle mrgnLeft5" onClick={this.props.deselectUserFunc.bind(null, receiver.id)}>
                <i class="fa fa-times"/>
              </button>
            </div>
            <div class="credentials">
              {
                Object.keys(webInfo).map(function(item){
                  return (
                      <div class="credential_container" key={item}>
                        <i class={classnames("fa", "mrgnRight5", webInfo[item].placeholderIcon)}/>
                        <input class="value_input input_unstyle"
                               placeholder={webInfo[item].placeholder}
                               type={webInfo[item].type}
                               name={item}
                               value={receiver.credentials[item]}
                               onChange={e => {this.props.handleUserCredentialInputFunc(receiver.id, item, e.target.value)}}/>
                      </div>
                  )
                }, this)
              }
            </div>
          </div>
      )
    }, this);

    return (
        <div>
          {selectedReceivers}
          <div class="modal_input_wrapper" onMouseDown={this.onMouseDown} onMouseUp={this.onMouseUp}>
            <input onFocus={this.setDropdown.bind(null, true)} class="input_unstyle" type="text" placeholder="Search for people..."/>
            <div class="floating_dropdown show" class={classnames("floating_dropdown", this.state.dropdown ? "show": null)}>
              <div class="dropdown_content">
                {this.props.receivers.map(function(item) {
                  return(
                      <div className={classnames("dropdown_row selectable", item.selected ? "selected" : null)}
                           key={item.id}
                           onClick={this.props.selectUserFunc.bind(null, item.id)}>
                        <span className="main_value">{item.username}{item.id === myId ? '(you)': null}</span>
                        {item.first_name != null &&
                        <span className="text-muted">&nbsp;- {item.first_name}&nbsp;{item.last_name}</span>}
                      </div>
                  )
                }, this)
                }
              </div>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamMultiAppUserSelect;