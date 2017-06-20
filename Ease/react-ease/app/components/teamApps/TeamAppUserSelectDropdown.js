var React = require('react');
var classnames = require('classnames');

class TeamAppUserSelectDropdown extends React.Component {
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
  render(){
    return (
        <div class="modal_input_wrapper" onMouseDown={this.onMouseDown} onMouseUp={this.onMouseUp}>
          {this.props.selectedReceivers.map(function (item) {
            return (
                <div class={classnames("receiver", item.accepted ? "accepted": null)} key={item.id}>
                                <span class="receiver_name">
                                  {item.username}
                                  {this.props.myId === item.id && "(you)"}
                                </span>
                  {item.can_see_information != undefined && this.props.changeReceiverSeePasswordPermFunc != undefined &&
                      <button class="button-unstyle mrgnLeft5" onClick={this.props.changeReceiverSeePasswordPermFunc.bind(null, item.id)}>
                        <i class={classnames("fa", item.can_see_information ? "fa-eye" : "fa-eye-slash")}/>
                      </button>
                  }
                  <button class="button-unstyle mrgnLeft5" onClick={this.props.deselectFunc.bind(null, item.id)}>
                    <i class="fa fa-times"/>
                  </button>
                </div>
            )
          }, this)}
          <input onFocus={this.setDropdown.bind(null, true)} class="input_unstyle" type="text" placeholder="Search for people..."/>
          <div class="floating_dropdown show" class={classnames("floating_dropdown", this.state.dropdown ? "show": null)}>
            <div class="dropdown_content">
              {this.props.receivers.map(function(item) {
                return(
                    <div className={classnames("dropdown_row selectable", item.selected ? "selected" : null)}
                         key={item.id}
                         onClick={this.props.selectFunc.bind(null, item.id)}>
                      <span className="main_value">{item.username}</span>
                      {item.first_name != null &&
                      <span className="text-muted">&nbsp;- {item.first_name}&nbsp;{item.last_name}</span>}
                    </div>
                )
              }, this)
              }
            </div>
          </div>
        </div>
    )
  }
}

module.exports = TeamAppUserSelectDropdown;