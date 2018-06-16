import React from 'react';

class TestComponent extends React.Component {
    render() {
        return (
            <div>
                <InsideComponent name={this.props.name}/>
            </div>
        );
    }
}

class InsideComponent extends React.Component {
    render() {
        return (
            <div>{this.props.name}</div>
        );
    }
}
export default TestComponent;
